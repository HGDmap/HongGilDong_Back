package hongik.map.honggildong.domain.review.service;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.image.service.ImageService;
import hongik.map.honggildong.domain.likes.repository.LikeRepository;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.converter.ReviewConverter;
import hongik.map.honggildong.domain.review.dto.ReviewRequestDTO;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.entity.Review;
import hongik.map.honggildong.domain.review.repository.ReviewRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final LikeRepository likeRepository;
    private final ImageService imageService;

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

    //특정 리뷰 1개
    @Override
    public ReviewResponseDTO.General getReviewById(Member member, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new GeneralException(ErrorStatus.REVIEW_NOT_FOUND));

        Boolean isLiked = likeRepository.existsByMemberAndReview(member,review);

        return ReviewConverter.toGeneralDTO(review, isLiked);
    }

    @Override
    @Transactional
    public Review createReviewOf(Member member, ReviewRequestDTO.create request, Facility facility) {

        Review review = ReviewConverter.toReview(member, facility, request);

        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteReviewOf(Member member, Long reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new GeneralException(ErrorStatus.REVIEW_NOT_FOUND));

        //본인확인
        if(!review.getMember().getId().equals(member.getId())){
            throw new GeneralException(ErrorStatus.NO_QUALIFICATION);
        }

        //이미지 모두 삭제
        imageService.deleteImages(review.getImages());
        //엔티티 삭제
        reviewRepository.delete(review);

    }

    @Override
    @Transactional
    public ReviewResponseDTO.General updateReviewOf(Member member, Long reviewId, ReviewRequestDTO.create request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new GeneralException(ErrorStatus.REVIEW_NOT_FOUND));

        //본인 확인
        if(!Objects.equals(review.getMember().getId(), member.getId())){
            throw new GeneralException(ErrorStatus.NO_QUALIFICATION);
        }

        List<String> newImageList = request.getPhotoList();
        List<String> removalTarget = review.getImages();
        removalTarget.removeAll(newImageList);

        Review updatedReview = review.update(request.getContent(), newImageList);

        imageService.deleteImages(removalTarget);

        Boolean isLiked = likeRepository.existsByMemberAndReview(member,updatedReview);


        return ReviewConverter.toGeneralDTO(updatedReview,isLiked);
    }
}
