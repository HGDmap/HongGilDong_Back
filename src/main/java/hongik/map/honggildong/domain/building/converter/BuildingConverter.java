package hongik.map.honggildong.domain.building.converter;

import hongik.map.honggildong.domain.building.dto.BuildingResponseDTO;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.entity.Floor;
import hongik.map.honggildong.domain.node.entity.Node;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildingConverter {
    public static BuildingResponseDTO.Detail toDetailDTO(Building building, Boolean isBookmarked, List<String> photoList) {

        Node node = building.getMainNode();
        List<Facility> facilities = building.getFacilities();

        List<BuildingResponseDTO.FloorFacility> floorFacilities = toFloorFacilityDTO(facilities,building);



        return BuildingResponseDTO.Detail.builder()
                .id(building.getId())
                .name(building.getName())
                .type("BUILDING")
                .description(building.getDescription())
                .isBookmarked(isBookmarked)
                .photoList(photoList)
                .floorFacilities(floorFacilities)
                .latitude(node.getLatitude())
                .longitude(node.getLongitude())
                .nodeId(node.getId())
                .nodeName(node.getName())
                .build();
    }

    private static List<BuildingResponseDTO.FloorFacility> toFloorFacilityDTO(List<Facility> facilities, Building building) {
// 층별로 묶기
        Map<Floor, List<BuildingResponseDTO.FacilityIdAndName>> floorMap = facilities.stream()
                .map(BuildingConverter::toFacilityIdAndName)
                .collect(Collectors.groupingBy(
                        fr -> building.getFacilities().stream()
                                .filter(f -> f.getId().equals(fr.getId()))
                                .findFirst()
                                .get()
                                .getFloor()
                ));

        // Map -> List 변환
        return floorMap.entrySet().stream()
                .map(e -> new BuildingResponseDTO.FloorFacility(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingInt(f -> Floor.getOrder(f.getFloor()))) //순서대로 정렬
                .toList();

    }

    private static BuildingResponseDTO.FacilityIdAndName toFacilityIdAndName(Facility facility) {

        return BuildingResponseDTO.FacilityIdAndName.builder()
                .name(facility.getName())
                .id(facility.getId())
                .build();
    }

}
