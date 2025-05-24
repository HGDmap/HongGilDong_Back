package hongik.map.honggildong.domain.review.service;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    //특정 멤버의 리뷰 리스트
    @Override
    public Page<Review> getReviewListOf(Member member, Pageable pageable) {
        return null;
    }

    //특정 시설의 리뷰 리스트
    @Override
    public Page<Review> getReviewListOf(Facility facility, Pageable pageable) {
        return null;
    }
}
