package hongik.map.honggildong.domain.likes.repository;

import hongik.map.honggildong.domain.likes.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes,Long> {

    //시설별 리뷰 보기에서 좋아요(스크랩) 여부를 위한 쿼리
    @Query("""
        SELECT
        l.review.id
        FROM Likes l
        WHERE l.member.id =:userId AND l.review.id IN :reviewIds
    """)
    List<Long> findAllLikedReviewId(@Param("userId") Long userId, @Param("reviewIds") List<Long> reviewIds);

    @Query("""
        SELECT
        l.review.id
        FROM Likes l
        WHERE l.member.id =:memberId AND l.review.id IN :reviews
    """)
    List<Long> findAllByReviewsAndMemberId(@Param("memberId") Long memberId, @Param("reviews") List<Long> reviewIds);

    //특정 리뷰에 좋아요 한 적 있는지 검사하는 메서드
    Boolean existsByMemberIdAndReviewIdAndStatus(Long memberId, Long reviewId, boolean status);

    Optional<Likes> findByMemberIdAndReviewId(Long id, Long reviewId);
}
