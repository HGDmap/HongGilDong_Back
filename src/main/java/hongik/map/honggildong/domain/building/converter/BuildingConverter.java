package hongik.map.honggildong.domain.building.converter;

import hongik.map.honggildong.domain.building.dto.BuildingResponseDTO;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.node.entity.Floor;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.domain.node.entity.NodeCode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BuildingConverter {
    public static BuildingResponseDTO.Detail toDetailDTO(Building building, Boolean isBookmarked, List<String> photoList) {

        List<Node> nodes = building.getNodes();
        nodes.sort(Comparator.comparingInt(n->Floor.getOrder(n.getFloor())));

        List<BuildingResponseDTO.FloorFacility> floorFacilities = new ArrayList<>();

        //층별 시설 저장
        for(Node node : nodes) {
            if(node.getCode()== NodeCode.FLOOR){
                List<Facility> facilities = node.getFacilities();
                floorFacilities.add(toFloorFacilityDTO(facilities,node.getFloor()));
            }
        }

        Node node = building.getMainNode();

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

    private static BuildingResponseDTO.FloorFacility toFloorFacilityDTO(List<Facility> facilities, Floor floor) {

        List<BuildingResponseDTO.FacilityIdAndName> facilityIdAndNames = facilities.stream().map(BuildingConverter::toFacilityIdAndName).toList();

        return BuildingResponseDTO.FloorFacility.builder()
                .floor(floor)
                .facilities(facilityIdAndNames)
                .build();

    }

    private static BuildingResponseDTO.FacilityIdAndName toFacilityIdAndName(Facility facility) {

        return BuildingResponseDTO.FacilityIdAndName.builder()
                .name(facility.getName())
                .id(facility.getId())
                .build();
    }

}
