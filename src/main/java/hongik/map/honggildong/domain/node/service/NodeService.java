package hongik.map.honggildong.domain.node.service;

import hongik.map.honggildong.domain.node.dto.NodeResponseDTO;

public interface NodeService {
    NodeResponseDTO.Coordinate getNodeCoordinate(Long nodeId);
}
