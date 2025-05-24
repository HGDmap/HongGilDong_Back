package hongik.map.honggildong.domain.review.service;

import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Override
    public Page<Review> getReviewListOf(Member member, Pageable pageable) {
        return null;
    }
}
