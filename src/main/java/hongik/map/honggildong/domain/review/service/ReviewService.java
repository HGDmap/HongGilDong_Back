package hongik.map.honggildong.domain.review.service;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.dto.ReviewRequestDTO;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    Page<Review> getReviewListOf(Member member, Pageable pageable);
    ReviewResponseDTO.GeneralPage getReviewListOf(Facility facility, Member member, Pageable pageable);

    ReviewResponseDTO.General getReviewById(Member member, Long reviewId);


    Review createReviewOf(Member member, ReviewRequestDTO.create request, Facility facility);

    void deleteReviewOf(Member member, Long reviewId);

    ReviewResponseDTO.General updateReviewOf(Member member, Long reviewId, ReviewRequestDTO.create request);
}
