package hongik.map.honggildong.domain.direction.service.pathfinding;

import hongik.map.honggildong.domain.edge.entity.Edge;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class AstarAlgorithm {

    private final Graph graph;

    // 보행 시간 계산 상수
    private static final double WALK_SPEED_MPS = 1.10;        // 평지/복도 보행 속도 (m/s) ≈ 4.7 km/h
    private static final double STAIRS_UP_SPEED_MPS = 0.60;   // 계단 오르기 (m/s)
    private static final double STAIRS_DOWN_SPEED_MPS = 0.80; // 계단 내리기 (m/s)
    private static final double ELEVATOR_SPEED_MPS = 1.50;    // 엘리베이터 수직 이동 속도 (m/s)
    private static final double ELEVATOR_DOOR_SEC = 4.0;      // 엘리베이터 문 여닫이 시간 (s)
    private static final double ELEVATOR_WAIT_SEC = 8.0;      // 평균 대기 시간 (s)

    private static double cmToM(Long cm) { return (cm == null) ? 0.0 : cm / 100.0; }

    public List<Long> findPath(Long startId, Long goalId) {
        Node start = graph.getNode(startId).orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));
        Node goal  = graph.getNode(goalId).orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));

        Map<Long, Double> gScore = new HashMap<>();
        Map<Long, Double> fScore = new HashMap<>();
        Map<Long, Long> cameFrom = new HashMap<>();

        Comparator<Long> cmp = Comparator.comparingDouble(id -> fScore.getOrDefault(id, Double.POSITIVE_INFINITY));
        PriorityQueue<Long> open = new PriorityQueue<>(cmp);

        gScore.put(start.getId(), 0.0);
        fScore.put(start.getId(), Heuristic.h(start, goal));
        open.add(start.getId());

        Set<Long> closed = new HashSet<>();

        while (!open.isEmpty()) {
            Long curId = open.poll();

            if (curId.equals(goal.getId())) {
                return reconstruct(cameFrom, curId);
            }
            closed.add(curId);

            for (Edge e : graph.edgesOf(curId)) {
                Long nbId = e.getEndNode().getId();
                if (closed.contains(nbId)) continue;

                double tentative = gScore.get(curId) + e.getCost();
                if (tentative < gScore.getOrDefault(nbId, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(nbId, curId);
                    gScore.put(nbId, tentative);

                    Node nb = e.getEndNode();
                    double h = Heuristic.h(nb, goal);
                    fScore.put(nbId, tentative + h);

                    open.remove(nbId);
                    open.add(nbId);
                }
            }
        }
        // 경로 없음
        throw new GeneralException(ErrorStatus.PATH_NOT_FOUND);
    }

    public Double findPathWithTime(Long startId, Long goalId) {
        List<Long> path = findPath(startId, goalId);
        double totalCost = sumEdgeCost(path);
        return estimateDurationSeconds(path);
    }

    // 비용 합산 (경로 상 연속 노드 쌍에 대한 Edge.cost 합)
    private double sumEdgeCost(List<Long> path) {
        if (path == null || path.size() < 2) return 0.0;
        double sum = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            Edge e = getEdgeBetween(path.get(i), path.get(i + 1));
            sum += e.getCost();
        }
        return sum;
    }

    // 보행 기준 소요시간 추정
    public double estimateDurationSeconds(List<Long> path) {
        if (path == null || path.size() < 2) return 0.0;

        double totalSec = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            Long fromId = path.get(i);
            Long toId   = path.get(i + 1);

            Node from = graph.getNode(fromId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));
            Node to = graph.getNode(toId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));

            Edge edge = getEdgeBetween(fromId, toId);
            totalSec += edgeDurationSeconds(from, to, edge);
        }
        return totalSec;
    }

    /**
     * 엣지별 시간 계산 로직
     * - 수평: 하버사인 거리 / 보행속도
     * - 수직(계단): Δz / 계단 속도 (상/하행 구분)
     * - 수직(엘리베이터): 대기 + 문여닫이 + (Δz / 엘리베이터 속도)
     * - 그 외: 수평 시간만
     */
    private double edgeDurationSeconds(Node from, Node to, Edge e) {
        Double lat1 = from.getLatitude();
        Double lon1 = from.getLongitude();
        Double lat2 = to.getLatitude();
        Double lon2 = to.getLongitude();

        double horizM = 0.0;
        if (lat1 != null && lon1 != null && lat2 != null && lon2 != null) {
            horizM = Heuristic.haversineMeters(lat1, lon1, lat2, lon2);
        }

        double z1 = cmToM(from.getHeight());
        double z2 = cmToM(to.getHeight());
        double dz = Math.abs(z1 - z2);

        var fromCode = from.getCode();
        var toCode   = to.getCode();

        boolean involvesStairs =
                (fromCode != null && fromCode.name().contains("STAIRS")) ||
                        (toCode   != null && toCode.name().contains("STAIRS"));
        boolean involvesElevator =
                (fromCode != null && fromCode.name().contains("ELEVATOR")) ||
                        (toCode   != null && toCode.name().contains("ELEVATOR"));

        // 기본 수평 보행 시간
        double timeSec = (horizM > 0) ? (horizM / WALK_SPEED_MPS) : 0.0;

        if (involvesStairs && dz > 0) {
            boolean goingUp = (z2 > z1);
            double stairSpeed = goingUp ? STAIRS_UP_SPEED_MPS : STAIRS_DOWN_SPEED_MPS;
            timeSec += dz / stairSpeed;
        } else if (involvesElevator && dz > 0) {
            timeSec += ELEVATOR_WAIT_SEC + ELEVATOR_DOOR_SEC + (dz / ELEVATOR_SPEED_MPS);
        }
        return timeSec;
    }

    // 경로 상 연속 노드 쌍의 엣지를 그래프에서 조회
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
