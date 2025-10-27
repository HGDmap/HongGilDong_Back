package hongik.map.honggildong.domain.bookmarkFolder.repository;

import hongik.map.honggildong.domain.bookmarkFolder.dto.JPQLBookmarkDTO;
import hongik.map.honggildong.domain.bookmarkFolder.entity.BookmarkFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookmarkFolderRepository extends JpaRepository<BookmarkFolder,Long> {

    @Query(value = """
    SELECT
        CASE
            WHEN b.building IS NOT NULL THEN 'BUILDING'
            WHEN b.facility IS NOT NULL THEN 'FACILITY'
        END AS type,
        COALESCE(b.building.id, b.facility.id) AS id
    FROM BookmarkFolder b
    WHERE (b.building.id IN :buildingIds
       OR b.facility.id IN :facilityIds)
       AND b.member.id = :userId
""")
    List<JPQLBookmarkDTO.SearchResult> findAllBookmarksFromSearch(@Param("userId") Long userId, @Param("buildingIds") List<Long> buildingIds, @Param("facilityIds") List<Long> facilityIds);
}
