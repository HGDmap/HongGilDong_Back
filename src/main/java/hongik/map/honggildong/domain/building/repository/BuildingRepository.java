package hongik.map.honggildong.domain.building.repository;

import hongik.map.honggildong.domain.building.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BuildingRepository extends JpaRepository<Building, Long> {

    @Query("""
    SELECT DISTINCT b
    FROM Building b
    LEFT JOIN FETCH b.facilities
    WHERE b.id = :buildingId
    """)
    Optional<Building> findWithFacilitiesById(@Param("id") Long buildingId);

}
