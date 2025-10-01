package hongik.map.honggildong.domain.node.service;

import hongik.map.honggildong.domain.node.dto.NodeResponseDTO;

import java.util.List;

public interface NodeService {
    NodeResponseDTO.Coordinate getNodeCoordinate(Long nodeId);

    List<NodeResponseDTO.Coordinate> getAllNodeCoordinates();
}
