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
    ReviewResponseDTO.GeneralPage getReviewListOf(Facility facility, Long memberId, Pageable pageable);

    ReviewResponseDTO.General getReviewById(Long memberId, Long reviewId);


    Review createReviewOf(Member member, ReviewRequestDTO.create request, Facility facility);

    void deleteReviewOf(Long memberId, Long reviewId);

    ReviewResponseDTO.General updateReviewOf(Long memberId, Long reviewId, ReviewRequestDTO.create request);
}
