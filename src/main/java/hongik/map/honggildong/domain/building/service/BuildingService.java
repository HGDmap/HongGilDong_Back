package hongik.map.honggildong.domain.building.service;

import hongik.map.honggildong.domain.building.dto.BuildingResponseDTO;
import hongik.map.honggildong.global.security.service.CustomUserDetails;

public interface BuildingService {
    BuildingResponseDTO.Detail getBuildingDetails(Long buildingId, CustomUserDetails userDetails);
}
