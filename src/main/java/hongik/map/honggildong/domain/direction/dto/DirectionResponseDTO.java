package hongik.map.honggildong.domain.direction.dto;

import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.domain.node.entity.NodeCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DirectionResponseDTO {
    private int minute;
    private int seconds;
    private List<PathNodes> nodes;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PathNodes {
        private Long nodeId;
        private String nodeName;
        private NodeCode nodeCode;
        private Double latitude;
        private Double longitude;
        private String image;
    }
}
