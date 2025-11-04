package hongik.map.honggildong.domain.review.repository;

import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
    SELECT DISTINCT r
    FROM Review r
    JOIN FETCH r.member
    LEFT JOIN FETCH r.images
    WHERE r.facility = :facility
    """)
    Page<Review> findAllByFacility(@Param("facility") Facility facility, Pageable pageable);
}
