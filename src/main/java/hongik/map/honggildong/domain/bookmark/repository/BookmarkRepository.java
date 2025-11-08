package hongik.map.honggildong.domain.bookmark.repository;

import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.bookmark.dto.JPQLBookmarkDTO;
import hongik.map.honggildong.domain.bookmarkFolder.entity.BookmarkFolder;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query(value = """
    SELECT
        CASE
            WHEN b.building IS NOT NULL THEN 'BUILDING'
            WHEN b.facility IS NOT NULL THEN 'FACILITY'
        END AS type,
        COALESCE(b.building.id, b.facility.id) AS id
    FROM Bookmark b
    WHERE (b.building.id IN :buildingIds
       OR b.facility.id IN :facilityIds)
       AND b.member.id = :userId
""")
    List<JPQLBookmarkDTO.SearchResult> findAllBookmarksFromSearch(@Param("userId") Long userId, @Param("buildingIds") List<Long> buildingIds, @Param("facilityIds") List<Long> facilityIds);

    Optional<Bookmark> findByFacilityAndMember(Facility facility, Member member);

    List<Bookmark> findAllByBookmarkFolder(BookmarkFolder folder);

    Optional<Bookmark> findByBuildingAndMember(Building building, Member member);

    Boolean existsByMemberAndFacility(Member member, Facility facility);
}
