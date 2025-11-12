package hongik.map.honggildong.domain.review.controller;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.service.FacilityService;
import hongik.map.honggildong.domain.likes.converter.LikeConverter;
import hongik.map.honggildong.domain.likes.dto.LikeResponseDTO;
import hongik.map.honggildong.domain.likes.entity.Likes;
import hongik.map.honggildong.domain.likes.repository.LikeRepository;
import hongik.map.honggildong.domain.likes.service.LikeService;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.member.service.MemberService;
import hongik.map.honggildong.domain.review.converter.ReviewConverter;
import hongik.map.honggildong.domain.review.dto.ReviewRequestDTO;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.entity.Review;
import hongik.map.honggildong.domain.review.repository.ReviewRepository;
import hongik.map.honggildong.domain.review.service.ReviewService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Tag(name = "리뷰")
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;
    private final LikeService likeService;
    private final FacilityService facilityService;

    //특정 리뷰 조회
    @GetMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 조회")
    public ApiResponse<ReviewResponseDTO.General> getReview(@PathVariable("reviewId") Long reviewId,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        //Member member = memberService.getMemberByUserDetails(userDetails);

        ReviewResponseDTO.General body = reviewService.getReviewById(userDetails.getMember(), reviewId);

        return ApiResponse.onSuccess(body);
    }

    //리뷰 생성
    @PostMapping("/{facilityId}")
    @Operation(summary = "리뷰 생성")
    public ApiResponse<ReviewResponseDTO.General> createReview(@RequestBody ReviewRequestDTO.create request,
                                                               @PathVariable("facilityId") Long facilityId,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        //Member member = memberService.getMemberByUserDetails(userDetails);
        Facility facility = facilityService.getFacilityById(facilityId);

        Review review = reviewService.createReviewOf(userDetails.getMember(), request, facility);
        ReviewResponseDTO.General body = ReviewConverter.toGeneralDTO(review, false);

        return ApiResponse.onSuccess(body);
    }

    //리뷰 삭제
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 삭제")
    public ApiResponse<String> deleteReview(@PathVariable("reviewId") Long reviewId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if(userDetails==null){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        Member member = userDetails.getMember();
        reviewService.deleteReviewOf(member, reviewId);

        return ApiResponse.onSuccess("삭제에 성공했습니다.");
    }

    //리뷰 수정
    @PatchMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 수정")
    public ApiResponse<ReviewResponseDTO.General> updateReview(@PathVariable("reviewId") Long reviewId,
                                                               @RequestBody ReviewRequestDTO.create request,
                                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = memberService.getMemberByUserDetails(userDetails);

        ReviewResponseDTO.General body = reviewService.updateReviewOf(userDetails.getMember(),reviewId, request);

        return ApiResponse.onSuccess(body);
    }

    //리뷰 좋아요/취소
    @PutMapping("/{reviewId}")
    @Operation(summary = "특정 리뷰 좋아요 등록 및 취소")
    public ApiResponse<LikeResponseDTO.General> createReview(@PathVariable Long reviewId,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = memberService.getMemberByUserDetails(userDetails);

        Likes like = likeService.createOrDeleteLikeOf(member,reviewId);
        LikeResponseDTO.General body = LikeConverter.toGeneralDTO(like);

        return ApiResponse.onSuccess(body);
    }

}
