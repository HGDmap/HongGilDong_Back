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
        b.longitude AS longitude
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
        n.longitude AS longitude
        FROM Event e JOIN Node n ON e.node_id = n.id
        WHERE n.name LIKE concat(:query,'%')
    
        UNION ALL
    
        SELECT
        'FACILITY' AS type,
        f.id AS id,
        f.name AS name,
        n.name AS description,
        f.main_img AS mainImg,
        n.latitude AS latitude,
        n.longitude AS longitude
        FROM facility f JOIN Node n ON f.node_id = n.id
        WHERE n.name LIKE concat(:query,'%')
    """, nativeQuery = true)
    List<Object[]> findAllType(@Param("query") String query);

}
