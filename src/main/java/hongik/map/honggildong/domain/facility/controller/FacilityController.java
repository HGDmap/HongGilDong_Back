package hongik.map.honggildong.domain.facility.controller;

import hongik.map.honggildong.domain.facility.converter.FacilityConverter;
import hongik.map.honggildong.domain.facility.dto.FacilityResponseDTO;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.service.FacilityService;
import hongik.map.honggildong.domain.image.dto.ImageRequestDTO;
import hongik.map.honggildong.domain.image.dto.ImageResponseDTO;
import hongik.map.honggildong.domain.image.service.ImageService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/facility")
@Tag(name = "시설")
@RequiredArgsConstructor
public class FacilityController {

    private final ReviewService reviewService;
    private final FacilityService facilityService;
    private final ImageService imageService;

    //특정 시설의 전체 리뷰 조회
    @GetMapping("/{facilityId}/reviews")
    @Operation(summary = "특정 시설의 전체 리뷰 조회")
    public ApiResponse<ReviewResponseDTO.GeneralPage> getFacilityReviews(@PathVariable("facilityId") Long facilityId,
                                                                          @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                          @ParameterObject Pageable pageable) {
        if(userDetails==null){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }
        Long memberId = userDetails.getMember().getId();
        Facility facility = facilityService.getFacilityById(facilityId);

        ReviewResponseDTO.GeneralPage body = reviewService.getReviewListOf(facility, memberId ,pageable);

        return ApiResponse.onSuccess(body);
    }

    //특정 시설의 전체 리뷰 조회
    @GetMapping("/{facilityId}/rating")
    @Operation(summary = "특정 시설의 전체 평점 및 추천 항목 현황 조회")
    public ApiResponse<FacilityResponseDTO.AvgRatingAndRecommendationStats> getFacilityRatings(@PathVariable("facilityId") Long facilityId,
                                                                                               @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                               @ParameterObject Pageable pageable) {
        if(userDetails==null){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        Facility facility = facilityService.getFacilityById(facilityId);

        FacilityResponseDTO.AvgRatingAndRecommendationStats body = facilityService.getAvgRatings(facility);

        return ApiResponse.onSuccess(body);
    }

    //특정 시설의 상세 정보 조회
    @GetMapping("/{facilityId}/details")
    @Operation(summary = "특정 시설의 상세 정보 조회")
    public ApiResponse<FacilityResponseDTO.Detail> getFacilityDetail(@PathVariable("facilityId") Long facilityId,
                                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {

        FacilityResponseDTO.Detail body = facilityService.getFacilityDetails(facilityId, userDetails);

        return ApiResponse.onSuccess(body);
    }

    //특정 시설 사진 모아보기
    @PostMapping("/{facilityId}/photos")
    @Operation(summary = "특정 시설의 사진 조회")
    public ApiResponse<ImageResponseDTO.ImagePage> getFacilityImages(@PathVariable("facilityId") Long facilityId,
                                                                     @RequestBody ImageRequestDTO.GetImagePageDTO request) {
        Facility facility = facilityService.getFacilityById(facilityId);

        String continuationToken = request.getContinuationToken();
        int size = request.getSize();

        ImageResponseDTO.ImagePage body = imageService.getPhotoPageOfFacility(facility, continuationToken, size);

        return ApiResponse.onSuccess(body);
    }

}
