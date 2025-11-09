package hongik.map.honggildong.domain.member.controller;

import hongik.map.honggildong.domain.likes.service.LikeService;
import hongik.map.honggildong.domain.member.converter.MemberConverter;
import hongik.map.honggildong.domain.member.dto.MemberRequestDTO;
import hongik.map.honggildong.domain.member.dto.MemberResponseDTO;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.member.service.MemberService;
import hongik.map.honggildong.domain.review.converter.ReviewConverter;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.entity.Review;
import hongik.map.honggildong.domain.review.service.ReviewService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="회원")
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService; //나중에 구현체로 변경
    private final ReviewService reviewService;
    private final LikeService likeService;

    //회원 정보 조회
    @GetMapping("")
    public ApiResponse<MemberResponseDTO.General> getMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = memberService.getMemberByUserDetails(userDetails);
        MemberResponseDTO.General body = MemberConverter.toGeneralDTO(member);

        return ApiResponse.onSuccess(body);
    }

    //회원 프로필 변경
    @PatchMapping("/mypage/profile")
    public ApiResponse<MemberResponseDTO.General> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody MemberRequestDTO.UpdateProfile request) {
        Member member = memberService.getMemberByUserDetails(userDetails);
        MemberResponseDTO.General body = MemberConverter.toGeneralDTO(memberService.updateProfile(member, request));

        return ApiResponse.onSuccess(body);
    }

    //회원 탈퇴
    @DeleteMapping("/delete")
    public ApiResponse<String> deleteMember(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Member member = memberService.getMemberByUserDetails(userDetails);
        memberService.deleteMember(member);

        return ApiResponse.onSuccess("탈퇴가 완료되었습니다.");
    }

    //내가 쓴 리뷰 리스트 조회
    @GetMapping("/mypage/reviews")
    public ApiResponse<ReviewResponseDTO.MyGeneralPage> getMyReviews(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @ParameterObject Pageable pageable) {
        Member member = memberService.getMemberByUserDetails(userDetails);
        Page<Review> reviews = reviewService.getReviewListOf(member,pageable);
        List<Long> likedReviewIds = new ArrayList<>();
        ReviewResponseDTO.MyGeneralPage body = ReviewConverter.toMyGeneralPage(reviews,likedReviewIds);

        return ApiResponse.onSuccess(body);
    }

    //내가 좋아요한 리뷰 리스트 조회
    @GetMapping("/mypage/likes")
    public ApiResponse<Page<ReviewResponseDTO.General>> getMyLikes(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @ParameterObject Pageable pageable) {
        Member member = memberService.getMemberByUserDetails(userDetails);
        Page<Review> reviews = likeService.getLikedReviewListOf(member,pageable);
        Page<ReviewResponseDTO.General> body = reviews.map(rw->ReviewConverter.toGeneralDTO(rw,true));

        return ApiResponse.onSuccess(body);
    }
}
