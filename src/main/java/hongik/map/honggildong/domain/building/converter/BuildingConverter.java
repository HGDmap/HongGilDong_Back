package hongik.map.honggildong.domain.building.converter;

import hongik.map.honggildong.domain.building.dto.BuildingResponseDTO;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.node.entity.Node;

import java.util.List;

public class BuildingConverter {
    public static BuildingResponseDTO.Detail toDetailDTO(Building building, Boolean isBookmarked, List<String> photoList) {

        Node node = building.getMainNode();

        return BuildingResponseDTO.Detail.builder()
                .id(building.getId())
                .name(building.getName())
                .type("BUILDING")
                .description(building.getDescription())
                .isBookmarked(isBookmarked)
                .photoList(photoList)
                .open(null)
                .phone(null)
                .link(null)
                .latitude(node.getLatitude())
                .longitude(node.getLongitude())
                .nodeId(node.getId())
                .nodeName(node.getName())
                .build();
    }

}
