package hongik.map.honggildong.domain.direction.service.pathfinding;

import hongik.map.honggildong.domain.edge.entity.Edge;
import hongik.map.honggildong.domain.node.entity.Node;

import java.util.*;

public class Graph {
    private final Map<String, Node> nodes = new HashMap<>();
    private final Map<String, List<Edge>> adj = new HashMap<>();

    public void addNode(Node n) {
        nodes.put(n.id(), n);
        adj.computeIfAbsent(n.id(), k -> new ArrayList<>());
    }

    public void addEdge(Edge e) {
        addNode(e.from());
        addNode(e.to());
        adj.computeIfAbsent(e.from().id(), k -> new ArrayList<>()).add(e);
    }

    public Optional<Node> getNode(String id) { return Optional.ofNullable(nodes.get(id)); }
    public List<Edge> edgesOf(String nodeId) { return adj.getOrDefault(nodeId, List.of()); }
}
