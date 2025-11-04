package hongik.map.honggildong.domain.facility.repository;

import hongik.map.honggildong.domain.facility.entity.Facility;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

    @Query("""
    SELECT f
    FROM Facility f
    JOIN FETCH f.node
    WHERE f.id =:facilityId
    """)
    @NotNull
    Optional<Facility> findById(@Param("facilityId") @NotNull Long facilityId);
}
