package hongik.map.honggildong.domain.likes.service;

import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.dto.ReviewResponseDTO;
import hongik.map.honggildong.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeService {
    ReviewResponseDTO.MyLikedGeneralPage getLikedReviewListOf(Member member, Pageable pageable);

    Boolean createOrDeleteLikeOf(Member member, Review review);
}
