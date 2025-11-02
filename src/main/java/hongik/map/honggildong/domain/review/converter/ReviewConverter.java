package hongik.map.honggildong.domain.review.converter;

import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.dto.jpql.JPQLReviewAndWriter;
import hongik.map.honggildong.domain.review.entity.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public class ReviewConverter {
    public static ReviewResponseDTO.General toGeneralDTO(JPQLReviewAndWriter review, Boolean isLiked) {
        return ReviewResponseDTO.General.builder()
                .id(review.getReviewId())
                .writerId(review.getWriterId())
                .writerNickname(review.getWriterNickname())
                .writerProfilePic(review.getWriterProfilePic())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                //.photoList(review.getPhotoList())
                .isLiked(isLiked)
                .build();
    }

    public static ReviewResponseDTO.GeneralPage toGeneralPageDTO(Page<JPQLReviewAndWriter> reviews, List<Long> likedReviewIds) {

        List<ReviewResponseDTO.General> content = reviews.getContent().stream().map(review -> {
            Boolean isLiked = likedReviewIds.contains(review.getReviewId());
            return toGeneralDTO(review, isLiked);
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

    public static ReviewResponseDTO.MyGeneral toMyGeneralDTO(Review review, Boolean isLiked) {
        return ReviewResponseDTO.MyGeneral.builder()
                .id(review.getId())
                .rating(review.getRating())
                .title(review.getTitle())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .photoList(review.getImages())
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
