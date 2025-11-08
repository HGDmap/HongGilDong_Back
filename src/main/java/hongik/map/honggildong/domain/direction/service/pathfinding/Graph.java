package hongik.map.honggildong.domain.direction.service.pathfinding;

import hongik.map.honggildong.domain.edge.entity.Edge;
import hongik.map.honggildong.domain.node.entity.Node;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Graph {
    private final Map<Long, Node> nodes = new HashMap<>();
    private final Map<Long, List<Edge>> adj = new HashMap<>();

    // 노드 엣지 초기화
    public void clear() {
        nodes.clear();
        adj.clear();
    }

    public void addNode(Node n) {
        nodes.put(n.getId(), n);
        adj.computeIfAbsent(n.getId(), k -> new ArrayList<>());
    }

    public void addEdge(Edge e) {
        addNode(e.getStartNode());
        addNode(e.getEndNode());
        adj.computeIfAbsent(e.getStartNode().getId(), k -> new ArrayList<>()).add(e);
    }

    public Optional<Node> getNode(Long id) { return Optional.ofNullable(nodes.get(id)); }
    public List<Edge> edgesOf(Long nodeId) { return adj.getOrDefault(nodeId, List.of()); }
}
