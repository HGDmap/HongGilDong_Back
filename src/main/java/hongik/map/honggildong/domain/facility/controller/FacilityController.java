package hongik.map.honggildong.domain.facility.controller;

import hongik.map.honggildong.domain.facility.converter.FacilityConverter;
import hongik.map.honggildong.domain.facility.dto.FacilityResponseDTO;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.service.FacilityService;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.service.ReviewService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facility")
@Tag(name = "시설")
@RequiredArgsConstructor
public class FacilityController {

    private final ReviewService reviewServiceImpl;
    private final FacilityService facilityServiceImpl;

    //특정 시설의 전체 리뷰 조회
    @GetMapping("/{facilityId}/reviews")
    @Operation(summary = "특정 시설의 전체 리뷰 조회")
    public ApiResponse<ReviewResponseDTO.GeneralPage> getFacilityReviews(@PathVariable("facilityId") Long facilityId,
                                                                          @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                          @ParameterObject Pageable pageable) {
        if(userDetails==null){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }
        Facility facility = facilityServiceImpl.getFacilityById(facilityId);

        ReviewResponseDTO.GeneralPage body = reviewServiceImpl.getReviewListOf(facility, userDetails.getMember(),pageable);

        return ApiResponse.onSuccess(body);
    }

    //특정 시설의 상세 정보 조회
    @GetMapping("/{facilityId}")
    @Operation(summary = "특정 시설의 상세 정보 조회")
    public ApiResponse<FacilityResponseDTO.Detail> getFacilityDetail(@PathVariable("facilityId") Long facilityId,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {

        Facility facility = facilityServiceImpl.getFacilityById(facilityId);
        FacilityResponseDTO.Detail body = FacilityConverter.toDetailDTO(facility);

        return ApiResponse.onSuccess(body);
    }

    //특정 시설 사진 모아보기
    @GetMapping("/{facilityId}/photos")
    @Operation(summary = "특정 시설의 전체 사진 조회")
    public ApiResponse<String> getFacilityPhotos(@PathVariable("facilityId") Long facilityId,
                                                 @ParameterObject Pageable pageable,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Facility facility = facilityServiceImpl.getFacilityById(facilityId);
        //PresignedUrl으로 저장 시 이미지 키 값을 시설i d로 구분하여 지정할 것

        return ApiResponse.onSuccess("Page<ImageDTO>");
    }

    //특정 시설 북마크 추가
    @PostMapping("/{facilityId}/bookmark")
    public ApiResponse<String> getFacilityReviews(@PathVariable("facilityId") Long facilityId) {
        Facility facility = facilityServiceImpl.getFacilityById(facilityId);
        //윤정 북마크 서비스 구현 후.createBookMark()
        return ApiResponse.onSuccess("나중에 bookmark status dto 추가");
    }

}
