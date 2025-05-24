package hongik.map.honggildong.domain.review.service;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.dto.ReviewRequestDTO;
import hongik.map.honggildong.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

public interface ReviewService {
    Page<Review> getReviewListOf(Member member, Pageable pageable);
    Page<Review> getReviewListOf(Facility facility, Pageable pageable);

    Review getReviewById(Long reviewId);

    Review createReviewOf(Member member, ReviewRequestDTO request);

    void deleteReviewOf(Member member, Review review);

    Review updateReviewOf(Member member, Review review);
}
