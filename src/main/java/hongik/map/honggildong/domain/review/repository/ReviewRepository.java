package hongik.map.honggildong.domain.review.repository;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.review.dto.jpql.JPQLReviewAndWriter;
import hongik.map.honggildong.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
    SELECT new hongik.map.honggildong.domain.review.dto.jpql.JPQLReviewAndWriter(
    r.id, r.content, m.id, m.nickname,m.profilePic,r.createdAt,r.updatedAt
    )
    FROM Review r
    JOIN r.member m
    WHERE r.facility = :facility
    
    """)
    Page<JPQLReviewAndWriter> findAllByFacility(@Param("facility") Facility facility, Pageable pageable);
}
