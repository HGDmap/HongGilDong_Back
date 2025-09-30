package hongik.map.honggildong.domain.node.dto;

import hongik.map.honggildong.domain.node.entity.NodeCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NodeResponseDTO {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Coordinate{
        private Long nodeId;
        private Double latitude;
        private Double longitude;
        private String name;
        private NodeCode nodeCode;
    }
}
