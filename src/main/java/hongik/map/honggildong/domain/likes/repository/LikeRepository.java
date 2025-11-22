package hongik.map.honggildong.domain.likes.repository;

import hongik.map.honggildong.domain.likes.entity.Likes;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //리뷰 아이디 리스트 내에 있는 리뷰 중 해당 유저가 좋아요한 리뷰 아이디 반환
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

    //단건 연관이므로 fetch join과 pageable 동시 수행 가능
    @Query(value = """
        SELECT l
        FROM Likes l
        JOIN FETCH l.review r
        JOIN FETCH r.member m
        WHERE l.member = :member AND l.status = true
    """,
    countQuery =  """
    SELECT COUNT(l)
    FROM Likes l
    WHERE l.member = :member AND l.status = true
    """)
    Page<Likes> findAllLikedReviewsByMember(@Param("member") Member member, Pageable pageable);
}
