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
    @GetMapping("/profile")
    @Operation(summary = "회원 정보 조회")
    public ApiResponse<MemberResponseDTO.General> getMember(@AuthenticationPrincipal CustomUserDetails userDetails) {

        if(userDetails==null){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        Member member = memberService.getMemberByUserDetails(userDetails);
        MemberResponseDTO.General body = MemberConverter.toGeneralDTO(member);

        return ApiResponse.onSuccess(body);
    }

    //회원 프로필 변경
    @PatchMapping("/mypage/profile")
    @Operation(summary = "회원 프로필 수정", description = "프로필 사진 삭제 시 빈 문자열로 보낼 것")
    public ApiResponse<MemberResponseDTO.General> updateMember(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody MemberRequestDTO.UpdateProfile request) {

        if(userDetails==null){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }
        Member member = memberService.getMemberByUserDetails(userDetails);
        MemberResponseDTO.General body = MemberConverter.toGeneralDTO(memberService.updateProfile(member, request));

        return ApiResponse.onSuccess(body);
    }

    //내가 쓴 리뷰 리스트 조회
    @GetMapping("/mypage/reviews")
    @Operation(summary = "내가 쓴 리뷰 조회")
    public ApiResponse<ReviewResponseDTO.MyGeneralPage> getMyReviews(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @ParameterObject Pageable pageable) {
        if(userDetails==null){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        Member member = userDetails.getMember();

        ReviewResponseDTO.MyGeneralPage body = reviewService.getReviewListOf(member,pageable);

        return ApiResponse.onSuccess(body);
    }

    //내가 좋아요한 리뷰 리스트 조회
    @GetMapping("/mypage/likes")
    @Operation(summary = "좋아요한 리뷰 리스트 조회")
    public ApiResponse<ReviewResponseDTO.MyLikedGeneralPage> getMyLikes(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @ParameterObject Pageable pageable) {
        if(userDetails==null){
            throw new GeneralException(ErrorStatus.UNAUTHORIZED);
        }

        Member member = userDetails.getMember();
        ReviewResponseDTO.MyLikedGeneralPage body = likeService.getLikedReviewListOf(member,pageable);

        return ApiResponse.onSuccess(body);
    }
}
