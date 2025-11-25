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
    private static final double STAIRS_UP_SEC_PER_LVL = 35.0;  // 레벨 1 올라갈 때 대략 몇 초
    private static final double STAIRS_DOWN_SEC_PER_LVL = 20.0;
    private static final double ELEVATOR_SEC_PER_LVL = 1.5;
    private static final double ELEVATOR_DOOR_SEC = 5.0;
    private static final double ELEVATOR_WAIT_SEC = 50.0;

    private static final double BASE_EDGE_SEC = 0.1;  // 엣지 하나 지날 때마다 기본 3초 페널티
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

        Comparator<Long> cmp = Comparator.comparingDouble(
                id -> fScore.getOrDefault(id, Double.POSITIVE_INFINITY)
        );
        PriorityQueue<Long> open = new PriorityQueue<>(cmp);

        gScore.put(start.getId(), 0.0);
        fScore.put(start.getId(), heuristicTime(start, goal));
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


                double edgeTime = edgeDurationSeconds(curNode, nb, e);


                double h1 = safeHeight(curNode);
                double h2 = safeHeight(nb);
                double heightEffect = Math.abs(h2 - h1);
                double heightCost   = HEIGHT_COST_WEIGHT * heightEffect;

                double edgeCost = edgeTime + heightCost;

                double tentative = gScore.get(curId) + edgeCost;

                // [LOG] 이웃 노드 후보 경로 정보
                log.info("[ASTAR]   Candidate: {} -> {} | edgeTime={}, heightCost={}, edgeCost={}, tentativeG={}",
                        curId, nbId, edgeTime, heightCost, edgeCost, tentative);


                double oldG = gScore.getOrDefault(nbId, Double.POSITIVE_INFINITY);
                if (tentative < gScore.getOrDefault(nbId, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(nbId, curId);
                    gScore.put(nbId, tentative);

                    double hTime        = heuristicTime(nb, goal);
                    double goalHeight     = safeHeight(goal);
                    double remainHeight = Math.abs(goalHeight - h2);
                    double hHeightCost  = HEIGHT_COST_WEIGHT * remainHeight;

                    double newF = tentative + hTime + hHeightCost;
                    fScore.put(nbId, newF);

                    // [LOG] 이 후보가 실제로 채택되었는지 (갱신되었는지)
                    log.info("[ASTAR]   -> UPDATE best path to node {} via {}, oldG={}, newG={}, newF={}",
                            nbId, curId, oldG, tentative, newF);

                    open.remove(nbId);
                    open.add(nbId);
                } else {
                    // [LOG] 기존 경로가 더 좋아서 버린 후보
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
