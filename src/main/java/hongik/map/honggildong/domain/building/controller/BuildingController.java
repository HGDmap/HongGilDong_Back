package hongik.map.honggildong.domain.building.controller;

import hongik.map.honggildong.domain.building.dto.BuildingResponseDTO;
import hongik.map.honggildong.domain.building.service.BuildingService;
import hongik.map.honggildong.domain.facility.dto.FacilityResponseDTO;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/building")
public class BuildingController {

    private final BuildingService buildingService;

    //특정 빌딩의 상세 정보 조회
    @GetMapping("/{buildingId}/details")
    @Operation(summary = "특정 시설의 상세 정보 조회")
    public ApiResponse<BuildingResponseDTO.Detail> getBuildingDetail(@PathVariable("buildingId") Long buildingId,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {

        BuildingResponseDTO.Detail body = buildingService.getBuildingDetails(buildingId, userDetails);

        return ApiResponse.onSuccess(body);
    }

}
