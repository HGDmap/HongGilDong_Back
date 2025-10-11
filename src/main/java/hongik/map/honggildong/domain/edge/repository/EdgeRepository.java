package hongik.map.honggildong.domain.edge.repository;

import hongik.map.honggildong.domain.edge.entity.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, Long> {
}
