package hongik.map.honggildong.domain.bookmark.repository;

import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
