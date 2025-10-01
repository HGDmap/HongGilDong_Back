package hongik.map.honggildong.domain.direction.service.pathfinding;

import hongik.map.honggildong.domain.edge.entity.Edge;
import hongik.map.honggildong.domain.node.entity.Node;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GraphLoader {
    private final Graph graph;

    @PostConstruct
    public void load() throws Exception {
        // TODO: 1) 노드 CSV 로딩 → graph.addNode(...)
        // TODO: 2) 엣지 로딩(이미 weight 포함) → graph.addEdge(new Edge(from, to, weight, type))

        // --- 예시(하드코딩) ---
        Node a = new Node("A", 37.551, 126.941, 1);
        Node b = new Node("B", 37.552, 126.942, 1);
        graph.addNode(a);
        graph.addNode(b);

        // 가정: 실내 동층 평면 거리(직선)로 이미 weight 계산됨
        double w = planarMeters(/*x1*/0, /*y1*/0, /*x2*/10, /*y2*/0);
        graph.addEdge(new Edge(a, b, w, EdgeType.INDOOR));
    }

    private double planarMeters(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2, dy = y1 - y2;
        return Math.hypot(dx, dy);
    }
}
