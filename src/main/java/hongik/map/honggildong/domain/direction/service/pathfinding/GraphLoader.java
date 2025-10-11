package hongik.map.honggildong.domain.direction.service.pathfinding;

import hongik.map.honggildong.domain.edge.entity.Edge;
import hongik.map.honggildong.domain.edge.repository.EdgeRepository;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.domain.node.repository.NodeRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GraphLoader {
    private final Graph graph;
    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;
    public static final double FLOOR_HEIGHT_M = 2.3;

    @PostConstruct
    public void load() {
        // 1. 모든 노드 정보를 DB에서 가져와 Graph에 추가
        // DB의 모든 노드를 메모리(Graph 객체)에 올려놓음
        List<Node> allNodes = nodeRepository.findAll();
        for (Node node : allNodes) {
            graph.addNode(node);
        }

        System.out.println("Node loading complete. Total nodes: " + allNodes.size());

        // 2. 모든 엣지 정보를 DB에서 가져와 동적으로 거리를 계산 후 Graph에 추가
        // "엣지는 end 노드와 start 노드의 code와 height를 참고해서 거리를 계산"
        List<Edge> allEdges = edgeRepository.findAll(); // DB 테이블과 매핑된 엔티티

        for (Edge edge : allEdges) {
            Node startNode = graph.getNode(edge.getStartNode().getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));
            Node endNode = graph.getNode(edge.getEndNode().getId())
                    .orElseThrow(() -> new GeneralException(ErrorStatus.NODE_NOT_FOUND));

            double calculatedWeight = calculateDistance(startNode, endNode);
            double calculatedWeightReverse = calculateDistance(endNode, startNode);

            // 계산된 가중치(weight)를 포함하여 새로운 Edge 객체를 생성하고 Graph에 추가
            Edge newEdge = Edge.builder()
                    .startNode(startNode)
                    .endNode(endNode)
                    .cost(calculatedWeight)
                    .build();
            graph.addEdge(newEdge);

            Edge newEdgeReverse = Edge.builder()
                    .startNode(endNode)
                    .endNode(startNode)
                    .cost(calculatedWeightReverse)
                    .build();
            graph.addEdge(newEdgeReverse);
        }
        System.out.println("Edge loading complete. Total edges: " + allEdges.size());
    }

    private double calculateDistance(Node startNode, Node endNode) {
        // 1. 위도/경도를 이용해 수평 거리를 '하버사인 공식'으로 계산
        double planarDistance = haversineMeters(
                startNode.getLatitude(), startNode.getLongitude(),
                endNode.getLatitude(), endNode.getLongitude()
        );

        // 2. 층(height)이 같은 경우, 수평 거리가 최종 거리
        if (Objects.equals(startNode.getHeight(), endNode.getHeight())) {
            return planarDistance;
        }
        // 3. 층이 다를 경우, 수직 높이 차이를 고려한 3D 직선 거리 계산
        else {
            double heightDifference = Math.abs(startNode.getHeight() - endNode.getHeight()) * FLOOR_HEIGHT_M;
            // 피타고라스 정리를 이용해 3D 대각선 거리 계산
            return Math.sqrt(Math.pow(planarDistance, 2) + Math.pow(heightDifference, 2));
        }
    }


    private double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS_M = 6_371_000.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1Rad) * Math.cos(lat2Rad);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_M * c;
    }
}
