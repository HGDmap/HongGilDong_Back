package hongik.map.honggildong.domain.facility.controller;

import hongik.map.honggildong.domain.facility.converter.FacilityConverter;
import hongik.map.honggildong.domain.facility.dto.FacilityResponseDTO;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.service.FacilityService;
import hongik.map.honggildong.domain.facility.service.FacilityServiceImpl;
import hongik.map.honggildong.domain.review.converter.ReviewConverter;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.service.ReviewService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ApiResponse<Page<ReviewResponseDTO.General>> getFacilityReviews(@PathVariable("facilityId") Long facilityId,
                                                                           @ParameterObject Pageable pageable) {
        Facility facility = facilityServiceImpl.getFacilityById(facilityId);
        Page<ReviewResponseDTO.General> body = reviewServiceImpl.getReviewListOf(facility,pageable).map(ReviewConverter::toGeneralDTO);

        return ApiResponse.onSuccess(body);
    }

    //특정 시설의 상세 정보 조회
    @GetMapping("/{facilityId}")
    public ApiResponse<FacilityResponseDTO.Detail> getFacilityDetail(@PathVariable("facilityId") Long facilityId) {

        Facility facility = facilityServiceImpl.getFacilityById(facilityId);
        FacilityResponseDTO.Detail body = FacilityConverter.toDetailDTO(facility);

        return ApiResponse.onSuccess(body);
    }

    //특정 시설 사진 모아보기
    @GetMapping("/{facilityId}/photos")
    public ApiResponse<String> getFacilityPhotos(@PathVariable("facilityId") Long facilityId,
                                                                           @ParameterObject Pageable pageable) {
        Facility facility = facilityServiceImpl.getFacilityById(facilityId);
        //이미지 엔티티 만들고 reviewId, facilityId 를 FK로 가지게 하도록 제안
        //(리뷰 외에는 이미지를 리스트로 가질 일이 없기 때문에 각 엔티티에서 String 필드로 S3 경로만 저장)

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
