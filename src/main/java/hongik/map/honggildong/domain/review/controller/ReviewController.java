package hongik.map.honggildong.domain.review.controller;

import hongik.map.honggildong.domain.like.converter.LikeConverter;
import hongik.map.honggildong.domain.like.dto.LikeResponseDTO;
import hongik.map.honggildong.domain.like.entity.Like;
import hongik.map.honggildong.domain.like.service.LikeService;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.member.service.MemberService;
import hongik.map.honggildong.domain.review.converter.ReviewConverter;
import hongik.map.honggildong.domain.review.dto.ReviewRequestDTO;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.entity.Review;
import hongik.map.honggildong.domain.review.service.ReviewService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Tag(name = "리뷰")
public class ReviewController {

    private final ReviewService reviewServiceImpl;
    private final MemberService memberServiceImpl;
    private final LikeService likeServiceImpl;

    //특정 리뷰 조회
    @GetMapping("/{reviewId}")
    public ApiResponse<ReviewResponseDTO.General> getReview(@PathVariable Long reviewId,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberServiceImpl.getMemberByUserDetails(userDetails);

        Review review = reviewServiceImpl.getReviewById(reviewId);
        ReviewResponseDTO.General body = ReviewConverter.toGeneralDTO(review);

        return ApiResponse.onSuccess(body);
    }

    //리뷰 생성
    @PostMapping("")
    public ApiResponse<ReviewResponseDTO.General> createReview(@RequestBody ReviewRequestDTO request,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberServiceImpl.getMemberByUserDetails(userDetails);

        Review review = reviewServiceImpl.createReviewOf(member, request);
        ReviewResponseDTO.General body = ReviewConverter.toGeneralDTO(review);

        return ApiResponse.onSuccess(body);
    }

    //리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ApiResponse<String> deleteReview(@PathVariable Long reviewId,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberServiceImpl.getMemberByUserDetails(userDetails);
        Review review = reviewServiceImpl.getReviewById(reviewId);
        reviewServiceImpl.deleteReviewOf(member, review);

        return ApiResponse.onSuccess("삭제에 성공했습니다.");
    }

    //리뷰 수정
    @PatchMapping("/{reviewId}")
    public ApiResponse<ReviewResponseDTO.General> updateReview(@PathVariable Long reviewId,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberServiceImpl.getMemberByUserDetails(userDetails);
        Review review = reviewServiceImpl.getReviewById(reviewId);

        ReviewResponseDTO.General body = ReviewConverter.toGeneralDTO(reviewServiceImpl.updateReviewOf(member, review));

        return ApiResponse.onSuccess(body);
    }

    //리뷰 좋아요/취소
    @PutMapping("/{reviewId}")
    public ApiResponse<LikeResponseDTO.General> createReview(@PathVariable Long reviewId,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberServiceImpl.getMemberByUserDetails(userDetails);
        Review review = reviewServiceImpl.getReviewById(reviewId);

        Like like = likeServiceImpl.createOrDeleteLikeOf(member,review);
        LikeResponseDTO.General body = LikeConverter.toGeneralDTO(like);

        return ApiResponse.onSuccess(body);
    }

}
