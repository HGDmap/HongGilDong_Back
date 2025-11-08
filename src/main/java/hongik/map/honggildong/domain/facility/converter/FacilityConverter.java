package hongik.map.honggildong.domain.facility.converter;

import hongik.map.honggildong.domain.facility.dto.FacilityResponseDTO;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.node.entity.Node;

import java.util.List;

public class FacilityConverter {

    public static FacilityResponseDTO.Detail toDetailDTO(Facility facility, Boolean isBookmarked, List<String> photoList) {

        Node node = facility.getNode();

        return FacilityResponseDTO.Detail.builder()
                .id(facility.getId())
                .type("FACILITY")
                .nodeId(node.getId())
                .nodeName(node.getName())
                .latitude(node.getLatitude())
                .longitude(node.getLongitude())
                .description(facility.getLocationDetail())
                .link(facility.getLink())
                .phone(facility.getPhone())
                .photoList(photoList)
                .isBookmarked(isBookmarked)
                .build();
    }

}
