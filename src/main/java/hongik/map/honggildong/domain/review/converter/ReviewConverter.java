package hongik.map.honggildong.domain.review.converter;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.likes.entity.Likes;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.dto.ReviewRequestDTO;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.entity.Review;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class ReviewConverter {

    public static Review toReview(Member member, Facility facility, ReviewRequestDTO.create request){

        return Review.builder()
                .member(member)
                .facility(facility)
                .content(request.getContent())
                .images(request.getPhotoList()==null ? new ArrayList<>() : request.getPhotoList())
                .build();
    }

    public static ReviewResponseDTO.General toGeneralDTO(Review review, Boolean isLiked, Long memberId) {

        Member writer = review.getMember();

        return ReviewResponseDTO.General.builder()
                .id(review.getId())
                .isMine(writer.getId().equals(memberId))
                .rating(review.getRating())
                .writerId(writer.getId())
                .writerNickname(writer.getNickname())
                .writerProfilePic(writer.getProfilePic())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .photoList(review.getImages())
                .isLiked(isLiked)
                .likedCnt(review.getLikedCnt())
                .build();
    }

    public static ReviewResponseDTO.GeneralPage toGeneralPageDTO(Page<Review> reviews, List<Long> likedReviewIds, Long memberId) {

        List<ReviewResponseDTO.General> content = reviews.getContent().stream().map(review -> {
            Boolean isLiked = likedReviewIds.contains(review.getId());
            return toGeneralDTO(review, isLiked, memberId);
        }).toList();

        return ReviewResponseDTO.GeneralPage.builder()
                .reviewList(content)
                .isFirst(reviews.isFirst())
                .isLast(reviews.isLast())
                .totalPages(reviews.getTotalPages())
                .totalElements(reviews.getTotalElements())
                .size(reviews.getSize())
                .build();
    }

    public static ReviewResponseDTO.GeneralPage toGeneralPageDTO(Page<Likes> likes, Long memberId){

        List<ReviewResponseDTO.General> content = likes.getContent().stream().map(l->toGeneralDTO(l.getReview(),true, memberId)).toList();

        return ReviewResponseDTO.GeneralPage.builder()
                .reviewList(content)
                .isFirst(likes.isFirst())
                .isLast(likes.isLast())
                .totalPages(likes.getTotalPages())
                .totalElements(likes.getTotalElements())
                .size(likes.getSize())
                .build();
    }

    public static ReviewResponseDTO.MyGeneral toMyGeneralDTO(Review review, Boolean isLiked) {

        Facility facility = review.getFacility();

        return ReviewResponseDTO.MyGeneral.builder()
                .id(review.getId())
                .rating(review.getRating())
                .facilityName(facility.getName())
                .facilityId(facility.getId())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .photoList(review.getImages())//N+1발생
                .isLiked(isLiked)
                .likedCnt(review.getLikedCnt())
                .build();
    }

    public static ReviewResponseDTO.MyGeneralPage toMyGeneralPage(Page<Review> reviews, List<Long> likedReviewIds) {
        List<ReviewResponseDTO.MyGeneral> content = reviews.getContent().stream().map(review -> {
            Boolean isLiked = likedReviewIds.contains(review.getId());
            return toMyGeneralDTO(review, isLiked);
        }).toList();

        return ReviewResponseDTO.MyGeneralPage.builder()
                .reviewList(content)
                .isFirst(reviews.isFirst())
                .isLast(reviews.isLast())
                .totalPages(reviews.getTotalPages())
                .totalElements(reviews.getTotalElements())
                .size(reviews.getSize())
                .build();

    }
}
