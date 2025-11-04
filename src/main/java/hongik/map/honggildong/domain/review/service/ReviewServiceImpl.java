package hongik.map.honggildong.domain.review.service;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.likes.repository.LikeRepository;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.converter.ReviewConverter;
import hongik.map.honggildong.domain.review.dto.ReviewRequestDTO;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.entity.Review;
import hongik.map.honggildong.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;

    //특정 멤버의 리뷰 리스트
    @Override
    public Page<Review> getReviewListOf(Member member, Pageable pageable) {
        return null;
    }

    //특정 시설의 리뷰 리스트
    @Override
    public ReviewResponseDTO.GeneralPage getReviewListOf(Facility facility, Member member, Pageable pageable) {

        Page<Review> reviewPage = reviewRepository.findAllByFacility(facility, pageable);
        List<Long> reviewIds = reviewPage.getContent().stream().map(Review::getId).toList();

        List<Long> likedReviews = likeRepository.findAllByReviewsAndMemberId(member.getId(),reviewIds);

        return ReviewConverter.toGeneralPageDTO(reviewPage, likedReviews);
    }

    @Override
    public Review getReviewById(Long reviewId) {
        return null;
    }

    @Override
    public Review createReviewOf(Member member, ReviewRequestDTO request) {
        return null;
    }

    @Override
    public void deleteReviewOf(Member member, Review review) {

    }

    @Override
    public Review updateReviewOf(Member member, Review review) {
        return null;
    }
}
