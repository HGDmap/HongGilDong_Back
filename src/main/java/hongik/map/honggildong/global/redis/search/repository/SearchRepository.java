package hongik.map.honggildong.global.redis.search.repository;

import hongik.map.honggildong.domain.building.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SearchRepository extends JpaRepository<Building, Long> {

    @Query(value = """
        SELECT
        'BUILDING' AS type,
        b.id AS id,
        b.name AS name,
        NULL AS description,
        b.main_img AS mainImg,
        b.latitude AS latitude,
        b.longitude AS longitude,
        b.main_node_id AS nodeId
        FROM building b
        WHERE b.name LIKE concat(:query,'%')
    
        UNION ALL
    
        SELECT
        'EVENT' AS type,
        e.id AS id,
        e.name AS name,
        n.name AS description,
        e.main_img AS mainImg,
        n.latitude AS latitude,
        n.longitude AS longitude,
        n.id AS nodeId
        FROM event e JOIN node n ON e.node_id = n.id
        WHERE e.name LIKE concat(:query,'%')
    
        UNION ALL
    
        SELECT
        'FACILITY' AS type,
        f.id AS id,
        f.name AS name,
        n.name AS description,
        f.main_img AS mainImg,
        n.latitude AS latitude,
        n.longitude AS longitude,
        n.id AS nodeId
        FROM facility f JOIN node n ON f.node_id = n.id
        WHERE f.name LIKE concat(:query,'%')
    """, nativeQuery = true)
    List<Object[]> findAllType(@Param("query") String query);

    @Query(value = """
        SELECT
        'BUILDING' AS type,
        b.id AS id,
        b.name AS name,
        b.alias AS alias,
        b.main_node_id AS nodeId
        FROM building b
    
        UNION ALL
    
        SELECT
        'EVENT' AS type,
        e.id AS id,
        e.name AS name,
        e.alias AS alias,
        e.node_id AS nodeId
        FROM event e
    
        UNION ALL
    
        SELECT
        'FACILITY' AS type,
        f.id AS id,
        f.name AS name,
        f.alias AS alias,
        f.node_id AS nodeId
        FROM facility f
    """, nativeQuery = true)
    List<Object[]> indexingAll();

}
