package hongik.map.honggildong.domain.node.converter;

import hongik.map.honggildong.domain.node.dto.NodeResponseDTO;
import hongik.map.honggildong.domain.node.entity.Node;

public class NodeConverter {

    public static NodeResponseDTO.Coordinate toCoordinateDTO(Node node) {
        return NodeResponseDTO.Coordinate.builder()
                .nodeId(node.getId())
                .name(node.getName())
                .latitude(node.getLatitude())
                .longitude(node.getLongitude())
                .nodeCode(node.getCode())
                .build();
    }
}
