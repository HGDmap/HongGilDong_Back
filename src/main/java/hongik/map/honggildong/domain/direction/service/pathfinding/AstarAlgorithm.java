package hongik.map.honggildong.domain.direction.service.pathfinding;
import hongik.map.honggildong.domain.edge.entity.Edge;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.global.apiPayload.code.BaseCode;
import lombok.RequiredArgsConstructor;

import java.util.*;


@Component
@RequiredArgsConstructor
public class AstarAlgorithm {
    private final Graph graph;
    private final BaseCode baseCode;

    public PathResult findPath(String startId, String goalId) {
        Node start = graph.getNode(startId).orElseThrow(() -> new baseCode.getReasonHttpStatus(ErrorCode.START_NODE_NOT_FOUND));
        Node goal  = graph.getNode(goalId).orElseThrow(() -> new baseCode.getReasonHttpStatus(ErrorCode.START_NODE_NOT_FOUND));

        Map<String, Double> gScore = new HashMap<>();
        Map<String, Double> fScore = new HashMap<>();
        Map<String, String> cameFrom = new HashMap<>();

        Comparator<String> cmp = Comparator.comparingDouble(id -> fScore.getOrDefault(id, Double.POSITIVE_INFINITY));
        PriorityQueue<String> open = new PriorityQueue<>(cmp);

        gScore.put(start.id(), 0.0);
        fScore.put(start.id(), Heuristic.h(start, goal));
        open.add(start.id());

        Set<String> closed = new HashSet<>();

        while (!open.isEmpty()) {
            String curId = open.poll();
            if (curId.equals(goal.id())) {
                List<String> path = reconstruct(cameFrom, curId);
                return new PathResult(path, gScore.get(curId));
            }
            closed.add(curId);

            for (Edge e : graph.edgesOf(curId)) {
                String nbId = e.to().id();
                if (closed.contains(nbId)) continue;

                double tentative = gScore.get(curId) + e.weight();
                if (tentative < gScore.getOrDefault(nbId, Double.POSITIVE_INFINITY)) {
                    cameFrom.put(nbId, curId);
                    gScore.put(nbId, tentative);

                    Node nb = e.to();
                    double h = Heuristic.h(nb, goal); // 또는 h3D(nb, goal)
                    fScore.put(nbId, tentative + h);

                    // 우선순위큐 갱신
                    open.remove(nbId);
                    open.add(nbId);
                }
            }
        }
        return PathResult.empty(); // 경로 없음
    }

    private List<String> reconstruct(Map<String, String> prev, String cur) {
        LinkedList<String> path = new LinkedList<>();
        while (cur != null) {
            path.addFirst(cur);
            cur = prev.get(cur);
        }
        return path;
    }

    public record PathResult(List<String> nodeIds, double totalCost) {
        public static PathResult empty() { return new PathResult(List.of(), Double.POSITIVE_INFINITY); }
        public boolean found() { return !nodeIds.isEmpty() && totalCost < Double.POSITIVE_INFINITY; }
    }
}
}
