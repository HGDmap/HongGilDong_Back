package hongik.map.honggildong.domain.node.repository;

import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.node.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
}
