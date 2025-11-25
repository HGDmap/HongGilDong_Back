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
    private static final double HEIGHT_COST_WEIGHT = 0.5;

    // 보행 시간 관련 상수
    private static final double WALK_SPEED_MPS = 1.10;
    private static final double STAIRS_UP_SEC_PER_LVL = 20.0;  // 레벨 1 올라갈 때 대략 몇 초
    private static final double STAIRS_DOWN_SEC_PER_LVL = 15.0;
    private static final double ELEVATOR_SEC_PER_LVL = 1.5;
    private static final double ELEVATOR_DOOR_SEC = 4.0;
    private static final double ELEVATOR_WAIT_SEC = 30.0;

    // 엘베-계단 이동
    private static final int SHORT_TRIP_HOP_THRESHOLD = 3;
    private static final double SHORT_TRIP_VERTICAL_PENALTY_SEC = 60.0;

    private static final double BASE_EDGE_SEC = 0.05;  // 엣지 하나 지날 때마다 기본 3초 페널티
    private static final double MIN_EDGE_SEC = 5.0;

    private static Double safeHeight(Node n) {
        return (double) n.getHeight();
    }

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
        lastVerticalMode.put(start.getId(), 0);
        lastVerticalHop.put(start.getId(), 0);
        open.add(start.getId());

        Set<Long> closed = new HashSet<>();

        // [LOG] 시작/목표 로그
        log.info("[ASTAR] Start findPath: startId={}, goalId={}", startId, goalId);
        log.info("[ASTAR] Initial fScore[{}]={}", start.getId(), fScore.get(start.getId()));

        while (!open.isEmpty()) {
            Long curId = open.poll();

            // [LOG] 현재 확장 중인 노드와 현재까지 비용
            log.info("[ASTAR] >>> Pop node={} from open-set, gScore={}, fScore={}",
                    curId,
                    gScore.getOrDefault(curId, Double.POSITIVE_INFINITY),
                    fScore.getOrDefault(curId, Double.POSITIVE_INFINITY)
            );

            if (curId.equals(goal.getId())) {
                List<Long> finalPath = reconstruct(cameFrom, curId);

                // [LOG] 최종 경로 출력
                log.info("[ASTAR] Goal reached. Final path: {}", finalPath);
                log.info("[ASTAR] Final total time (sec): {}",
                        estimateDurationSeconds(finalPath));

                return finalPath;
            }
            closed.add(curId);

            Node curNode = graph.getNode(curId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));

            for (Edge e : graph.edgesOf(curId)) {
                Long nbId = e.getEndNode().getId();
                if (closed.contains(nbId)) continue;

                Node nb = e.getEndNode();

                // --- 1) hop(엣지 수) 계산 ---
                int curHop = hopCount.getOrDefault(curId, 0);
                int newHop = curHop + 1;

                // --- 2) 기본 물리 시간 (거리 + 층수) ---
                double edgeTime = edgeDurationSeconds(curNode, nb, e);

                // --- 3) 이 엣지가 계단/엘베인지 판단 ---
                var fromCode = curNode.getCode();
                var toCode = nb.getCode();

                boolean involvesStairs =
                        (fromCode != null && fromCode.name().contains("STAIR")) ||
                                (toCode != null && toCode.name().contains("STAIR"));

                boolean involvesElevator =
                        (fromCode != null && fromCode.name().contains("ELEVATOR")) ||
                                (toCode != null && toCode.name().contains("ELEVATOR"));

                int currentMode = 0; // 0: 없음, 1: 계단, 2: 엘베
                if (involvesStairs) currentMode = 1;
                else if (involvesElevator) currentMode = 2;

                int prevMode = lastVerticalMode.getOrDefault(curId, 0);
                int prevVerticalHop = lastVerticalHop.getOrDefault(curId, 0);

                // --- 4) 시작 후 3엣지 이내에서 계단/엘베를 쓰면 패널티 ---
                if (currentMode != 0 && newHop <= SHORT_TRIP_HOP_THRESHOLD) {
                    edgeTime += SHORT_TRIP_VERTICAL_PENALTY_SEC;
                }

                // --- 5) 3엣지 이내 계단↔엘베 모드 전환이면 추가 패널티 ---
                if (currentMode != 0 && prevMode != 0 &&
                        currentMode != prevMode &&
                        prevVerticalHop > 0 &&
                        (newHop - prevVerticalHop) <= SHORT_TRIP_HOP_THRESHOLD) {
                    edgeTime += SHORT_TRIP_VERTICAL_PENALTY_SEC;
                }

                // --- 6) 기존 높이 기반 cost 그대로 사용 ---
                double h1 = safeHeight(curNode);
                double h2 = safeHeight(nb);
                double heightEffect = Math.abs(h2 - h1);
                double heightCost   = HEIGHT_COST_WEIGHT * heightEffect;

                double edgeCost = edgeTime + heightCost;
                double tentative = gScore.get(curId) + edgeCost;

                log.info("[ASTAR]   Candidate: {} -> {} | edgeTime={}, heightCost={}, edgeCost={}, tentativeG={}",
                        curId, nbId, edgeTime, heightCost, edgeCost, tentative);

                double oldG = gScore.getOrDefault(nbId, Double.POSITIVE_INFINITY);
                if (tentative < oldG) {
                    cameFrom.put(nbId, curId);
                    gScore.put(nbId, tentative);
                    hopCount.put(nbId, newHop);

                    // 수직 모드를 실제로 썼다면, "마지막 수직 모드" 갱신
                    int newPrevMode = prevMode;
                    int newPrevHop = prevVerticalHop;
                    if (currentMode != 0) {
                        newPrevMode = currentMode;
                        newPrevHop = newHop;
                    }
                    lastVerticalMode.put(nbId, newPrevMode);
                    lastVerticalHop.put(nbId, newPrevHop);

                    double hTime        = heuristicTime(nb, goal);
                    double goalHeight   = safeHeight(goal);
                    double remainHeight = Math.abs(goalHeight - h2);
                    double hHeightCost  = HEIGHT_COST_WEIGHT * remainHeight;

                    double newF = tentative + hTime + hHeightCost;
                    fScore.put(nbId, newF);

                    log.info("[ASTAR]   -> UPDATE best path to node {} via {}, oldG={}, newG={}, newF={}",
                            nbId, curId, oldG, tentative, newF);

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


    public Double findPathWithTime(Long startId, Long goalId) {
        List<Long> path = findPath(startId, goalId);
        return estimateDurationSeconds(path);
    }


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
            totalSec += edgeDurationSeconds(from, to, edge);
        }
        return totalSec;
    }


    private double edgeDurationSeconds(Node from, Node to, Edge e) {
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
        double dLevel = Math.abs(h1 - h2); // 상대고도 레벨 차이

        var fromCode = from.getCode();
        var toCode = to.getCode();

        boolean involvesStairs =
                (fromCode != null && fromCode.name().contains("STAIR")) ||
                        (toCode != null && toCode.name().contains("STAIR"));

        boolean involvesElevator =
                (fromCode != null && fromCode.name().contains("ELEVATOR")) ||
                        (toCode != null && toCode.name().contains("ELEVATOR"));

        // 1) 수평 이동 시간
//        double timeSec = (horizM > 0) ? (horizM / WALK_SPEED_MPS) : 0.0;
        double timeSec = BASE_EDGE_SEC;

        if (horizM > 0) {
            timeSec += horizM / WALK_SPEED_MPS;
        } else {
            timeSec += MIN_EDGE_SEC; // 같은 위치여도 최소 시간
        }

        // 2) 수직 이동 시간 (상대고도 레벨 기반)
        if (involvesStairs && dLevel > 0) {
            boolean goingUp = (h2 > h1);
            double perLevel = goingUp ? STAIRS_UP_SEC_PER_LVL : STAIRS_DOWN_SEC_PER_LVL;
            timeSec += dLevel * perLevel;
        } else if (involvesElevator && dLevel > 0) {
            timeSec += ELEVATOR_WAIT_SEC + ELEVATOR_DOOR_SEC + (dLevel * ELEVATOR_SEC_PER_LVL);
        }

        return timeSec;
    }


    private Edge getEdgeBetween(Long fromId, Long toId) {
        return graph.edgesOf(fromId).stream()
                .filter(ed -> Objects.equals(ed.getEndNode().getId(), toId))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.PATH_NOT_FOUND));
    }

    private List<Long> reconstruct(Map<Long, Long> prev, Long cur) {
        LinkedList<Long> path = new LinkedList<>();
        while (cur != null) {
            path.addFirst(cur);
            cur = prev.get(cur);
        }
        return path;
    }
}
