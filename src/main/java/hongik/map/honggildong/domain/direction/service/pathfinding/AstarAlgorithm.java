package hongik.map.honggildong.domain.direction.service.pathfinding;

import hongik.map.honggildong.domain.edge.entity.Edge;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class AstarAlgorithm {

    private final Graph graph;

    // 고도(층수) 기반 추가 불편도 가중치
    private static final double HEIGHT_COST_WEIGHT = 0.5;

    // 보행 시간 관련 상수
    private static final double WALK_SPEED_MPS = 1.10;
    private static final double STAIRS_UP_SEC_PER_LVL = 20.0;    // 층/레벨 1 올라갈 때 대략 몇 초
    private static final double STAIRS_DOWN_SEC_PER_LVL = 15.0;  // 층/레벨 1 내려갈 때 대략 몇 초
    private static final double ELEVATOR_SEC_PER_LVL = 1.5;      // 엘베로 층 1 이동 시 소요 시간
    private static final double ELEVATOR_DOOR_SEC = 4.0;         // 엘베 문 열고 닫히는 시간
    private static final double ELEVATOR_WAIT_SEC = 30.0;        // 엘베 평균 대기 시간

    // 엘베/계단 사용에 대한 단거리 이동 패널티
    private static final int SHORT_TRIP_HOP_THRESHOLD = 3;
    private static final double SHORT_TRIP_VERTICAL_PENALTY_SEC = 60.0;

    // 엣지 통과 기본 오버헤드 및 최소 시간
    private static final double BASE_EDGE_SEC = 0.05;  // 엣지 하나 지날 때 고정적으로 더해줄 아주 작은 오버헤드
    private static final double MIN_EDGE_SEC = 5.0;    // 수평거리 0이어도 최소 5초는 걸린다고 가정

    // 실내/실외 전환 패널티
    private static final double OUTDOOR_TRANSITION_PENALTY_SEC = 50.0;

    // 수직 이동 모드 구분 상수
    private static final int VERTICAL_NONE = 0;
    private static final int VERTICAL_STAIRS = 1;
    private static final int VERTICAL_ELEVATOR = 2;

    private static Double safeHeight(Node n) {
        return (double) n.getHeight();
    }

    /**
     * A*를 이용해 startId ~ goalId까지 "검색용 시간 코스트" 기준 최단 경로를 찾는다.
     */
    public List<Long> findPath(Long startId, Long goalId) {
        Node start = graph.getNode(startId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));
        Node goal = graph.getNode(goalId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));

        // gScore: 시작 ~ 현재까지 누적 "시간(초)"
        Map<Long, Double> gScore = new HashMap<>();
        // fScore: gScore + 휴리스틱(추정 잔여시간)
        Map<Long, Double> fScore = new HashMap<>();
        // 경로 복원용
        Map<Long, Long> cameFrom = new HashMap<>();

        // hop 수, 최근 수직 모드, 최근 수직 모드 사용 hop
        Map<Long, Integer> hopCount = new HashMap<>();
        Map<Long, Integer> lastVerticalMode = new HashMap<>();
        Map<Long, Integer> lastVerticalHop = new HashMap<>();

        Comparator<Long> cmp = Comparator.comparingDouble(
                id -> fScore.getOrDefault(id, Double.POSITIVE_INFINITY)
        );
        PriorityQueue<Long> open = new PriorityQueue<>(cmp);

        gScore.put(start.getId(), 0.0);
        fScore.put(start.getId(), heuristicTime(start, goal));
        hopCount.put(start.getId(), 0);
        lastVerticalMode.put(start.getId(), VERTICAL_NONE);
        lastVerticalHop.put(start.getId(), 0);
        open.add(start.getId());

        Set<Long> closed = new HashSet<>();

        log.info("[ASTAR] Start findPath: startId={}, goalId={}", startId, goalId);
        log.info("[ASTAR] Initial fScore[{}]={}", start.getId(), fScore.get(start.getId()));

        while (!open.isEmpty()) {
            Long curId = open.poll();

            log.info("[ASTAR] >>> Pop node={} from open-set, gScore={}, fScore={}",
                    curId,
                    gScore.getOrDefault(curId, Double.POSITIVE_INFINITY),
                    fScore.getOrDefault(curId, Double.POSITIVE_INFINITY)
            );

            if (curId.equals(goal.getId())) {
                List<Long> finalPath = reconstruct(cameFrom, curId);
                double totalCost = gScore.getOrDefault(curId, 0.0);

                log.info("[ASTAR] Goal reached. Final path: {}", finalPath);
                log.info("[ASTAR] Final total time (sec, same as A* gScore): {}", totalCost);

                return finalPath;
            }
            closed.add(curId);

            Node curNode = graph.getNode(curId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));

            for (Edge e : graph.edgesOf(curId)) {
                Long nbId = e.getEndNode().getId();
                if (closed.contains(nbId)) continue;

                Node nb = e.getEndNode();

                int curHop = hopCount.getOrDefault(curId, 0);
                int newHop = curHop + 1;

                int prevMode = lastVerticalMode.getOrDefault(curId, VERTICAL_NONE);
                int prevVerticalHop = lastVerticalHop.getOrDefault(curId, 0);

                double edgeCost = edgeSearchCostSeconds(curNode, nb, e, newHop, prevMode, prevVerticalHop);
                double tentative = gScore.get(curId) + edgeCost;

                log.info("[ASTAR]   Candidate: {} -> {} | edgeCost={}, tentativeG={}",
                        curId, nbId, edgeCost, tentative);

                double oldG = gScore.getOrDefault(nbId, Double.POSITIVE_INFINITY);
                if (tentative < oldG) {
                    cameFrom.put(nbId, curId);
                    gScore.put(nbId, tentative);
                    hopCount.put(nbId, newHop);

                    // 수직 모드를 실제로 사용했다면 갱신
                    int currentMode = getVerticalMode(curNode, nb);
                    int newPrevMode = prevMode;
                    int newPrevHop = prevVerticalHop;
                    if (currentMode != VERTICAL_NONE) {
                        newPrevMode = currentMode;
                        newPrevHop = newHop;
                    }
                    lastVerticalMode.put(nbId, newPrevMode);
                    lastVerticalHop.put(nbId, newPrevHop);

                    double hTime = heuristicTime(nb, goal);
                    double newF = tentative + hTime;
                    fScore.put(nbId, newF);

                    log.info("[ASTAR]   -> UPDATE best path to node {} via {}, oldG={}, newG={}, newF={}",
                            nbId, curId, oldG, tentative, newF);

                    // PriorityQueue 내 기존 요소 제거 후 재삽입
                    open.remove(nbId);
                    open.add(nbId);
                } else {
                    log.info("[ASTAR]   -> SKIP candidate via {} to {} (oldG={} <= tentativeG={})",
                            curId, nbId, oldG, tentative);
                }
            }
        }

        log.warn("[ASTAR] Path not found from {} to {}", startId, goalId);
        throw new GeneralException(ErrorStatus.PATH_NOT_FOUND);
    }

    /**
     * 경로를 찾고, 그 경로의 "물리적인 예상 소요 시간(초)"를 리턴한다.
     */
    public Double findPathWithTime(Long startId, Long goalId) {
        List<Long> path = findPath(startId, goalId);
        return estimateDurationSeconds(path);
    }

    /**
     * 휴리스틱: 단순 수평 직선거리 / 보행 속도로 "최소 시간" 추정.
     * 수직 이동, 패널티 등은 반영하지 않음 (안전한 휴리스틱 유지).
     */
    private double heuristicTime(Node from, Node goal) {
        if (from.getLatitude() == null || from.getLongitude() == null
                || goal.getLatitude() == null || goal.getLongitude() == null) {
            return 0.0;
        }
        double straightMeters = Heuristic.haversineMeters(
                from.getLatitude(), from.getLongitude(),
                goal.getLatitude(), goal.getLongitude()
        );
        return straightMeters / WALK_SPEED_MPS;  // 평지 기준 최단 시간
    }

    /**
     * 최종 경로에 대해 "사용자에게 보여줄 예상 소요 시간(초)" 계산.
     * 엣지별 물리 시간을 합산한다.
     */
    public double estimateDurationSeconds(List<Long> path) {
        if (path == null || path.size() < 2) return 0.0;

        double totalSec = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            Long fromId = path.get(i);
            Long toId = path.get(i + 1);

            Node from = graph.getNode(fromId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));
            Node to = graph.getNode(toId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));

            Edge edge = getEdgeBetween(fromId, toId);
            totalSec += edgePhysicalSeconds(from, to, edge);
        }
        return totalSec;
    }

    /**
     * 두 노드 사이의 Edge를 가져온다. 없으면 예외.
     */
    private Edge getEdgeBetween(Long fromId, Long toId) {
        return graph.edgesOf(fromId).stream()
                .filter(ed -> Objects.equals(ed.getEndNode().getId(), toId))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.PATH_NOT_FOUND));
    }

    /**
     * prevMode, prevVerticalHop을 고려한 "검색용" 엣지 코스트.
     * - 기본 물리 시간(edgePhysicalSeconds)
     * - 단거리 수직 이동 패널티
     * - 수직 모드 전환 패널티
     * - 고도 차이에 따른 불편도
     * - 실내/실외 전환 패널티
     */
    private double edgeSearchCostSeconds(
            Node from,
            Node to,
            Edge e,
            int newHop,
            int prevMode,
            int prevVerticalHop
    ) {
        double cost = edgePhysicalSeconds(from, to, e);

        // 1) 현재 엣지 수직 모드
        int currentMode = getVerticalMode(from, to);

        // 2) 짧은 이동 내에서 계단/엘베 사용 패널티
        if (currentMode != VERTICAL_NONE && newHop <= SHORT_TRIP_HOP_THRESHOLD) {
            cost += SHORT_TRIP_VERTICAL_PENALTY_SEC;
        }

        // 3) 짧은 이동 내 계단 ↔ 엘베 전환 패널티
        if (currentMode != VERTICAL_NONE && prevMode != VERTICAL_NONE &&
                currentMode != prevMode &&
                prevVerticalHop > 0 &&
                (newHop - prevVerticalHop) <= SHORT_TRIP_HOP_THRESHOLD) {
            cost += SHORT_TRIP_VERTICAL_PENALTY_SEC;
        }

        // 4) 높이 기반 추가 cost (불편도) – 시간 단위로 그대로 더해줌
        double h1 = safeHeight(from);
        double h2 = safeHeight(to);
        double heightEffect = Math.abs(h2 - h1);
        cost += HEIGHT_COST_WEIGHT * heightEffect;

        // 5) outdoor ↔ 그 외(실내 등) 환경 전환 패널티
        boolean fromOutdoor = isOutdoor(from);
        boolean toOutdoor = isOutdoor(to);
        if (fromOutdoor != toOutdoor) {
            cost += OUTDOOR_TRANSITION_PENALTY_SEC;
        }

        return cost;
    }

    /**
     * 수직 이동 모드 결정:
     * - 계단 포함: VERTICAL_STAIRS
     * - 엘리베이터 포함: VERTICAL_ELEVATOR
     * - 그 외: VERTICAL_NONE
     */
    private int getVerticalMode(Node from, Node to) {
        var fromCode = from.getCode();
        var toCode = to.getCode();

        boolean involvesStairs =
                (fromCode != null && fromCode.name().contains("STAIR")) ||
                        (toCode != null && toCode.name().contains("STAIR"));

        boolean involvesElevator =
                (fromCode != null && fromCode.name().contains("ELEVATOR")) ||
                        (toCode != null && toCode.name().contains("ELEVATOR"));

        if (involvesStairs) return VERTICAL_STAIRS;
        if (involvesElevator) return VERTICAL_ELEVATOR;
        return VERTICAL_NONE;
    }

    /**
     code 이름에 OUTDOOR가 들어가면 야외로 간주.
     */
    private boolean isOutdoor(Node n) {
        var code = n.getCode();
        return code != null && code.name().contains("OUTDOOR");
    }

    /**
     * "물리적인" 엣지 소요 시간(초) 계산:
     * - 수평 이동 시간
     * - 엣지 최소/기본 오버헤드
     * - 계단/엘베에 따른 층 이동 시간, 엘베 대기 및 문 여닫힘 시간
     */
    private double edgePhysicalSeconds(Node from, Node to, Edge e) {
        Double lat1 = from.getLatitude();
        Double lon1 = from.getLongitude();
        Double lat2 = to.getLatitude();
        Double lon2 = to.getLongitude();

        double horizM = 0.0;
        if (lat1 != null && lon1 != null && lat2 != null && lon2 != null) {
            horizM = Heuristic.haversineMeters(lat1, lon1, lat2, lon2);
        }

        double h1 = safeHeight(from);
        double h2 = safeHeight(to);
        double dLevel = Math.abs(h1 - h2);

        var fromCode = from.getCode();
        var toCode = to.getCode();

        boolean involvesStairs =
                (fromCode != null && fromCode.name().contains("STAIR")) ||
                        (toCode != null && toCode.name().contains("STAIR"));

        boolean involvesElevator =
                (fromCode != null && fromCode.name().contains("ELEVATOR")) ||
                        (toCode != null && toCode.name().contains("ELEVATOR"));

        // 1) 수평 이동
        double timeSec = BASE_EDGE_SEC;
        if (horizM > 0) {
            timeSec += horizM / WALK_SPEED_MPS;
        } else {
            timeSec += MIN_EDGE_SEC; // 같은 위치여도 최소 시간
        }

        // 2) 수직 이동(물리적으로 드는 시간)
        if (involvesStairs && dLevel > 0) {
            boolean goingUp = (h2 > h1);
            double perLevel = goingUp ? STAIRS_UP_SEC_PER_LVL : STAIRS_DOWN_SEC_PER_LVL;
            timeSec += dLevel * perLevel;
        } else if (involvesElevator && dLevel > 0) {
            timeSec += ELEVATOR_WAIT_SEC + ELEVATOR_DOOR_SEC + (dLevel * ELEVATOR_SEC_PER_LVL);
        }

        return timeSec;
    }

    /**
     * prev map을 따라가며 경로를 복원한다.
     */
    private List<Long> reconstruct(Map<Long, Long> prev, Long cur) {
        LinkedList<Long> path = new LinkedList<>();
        while (cur != null) {
            path.addFirst(cur);
            cur = prev.get(cur);
        }
        return path;
    }

}