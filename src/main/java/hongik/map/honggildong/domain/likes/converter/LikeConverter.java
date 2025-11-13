package hongik.map.honggildong.domain.likes.converter;

import hongik.map.honggildong.domain.likes.dto.LikeResponseDTO;
import hongik.map.honggildong.domain.likes.entity.Likes;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.entity.Review;

public class LikeConverter {

    public static Likes toLikes(Member member, Review review, Boolean status){
        return Likes.builder()
                .member(member)
                .review(review)
                .status(status)
                .build();
    }

    public static LikeResponseDTO.General toGeneralDTO(Boolean isLiked, Long reviewLikedCnt){
        return LikeResponseDTO.General.builder()
                .isLiked(isLiked)
                .reviewLikedCnt(reviewLikedCnt)
                .build();
    }
}
