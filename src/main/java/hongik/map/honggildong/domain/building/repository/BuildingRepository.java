package hongik.map.honggildong.domain.building.repository;

import hongik.map.honggildong.domain.building.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Long> {
}
