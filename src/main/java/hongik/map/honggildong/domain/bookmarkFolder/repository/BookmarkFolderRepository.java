package hongik.map.honggildong.domain.bookmarkFolder.repository;

import hongik.map.honggildong.domain.bookmarkFolder.entity.BookmarkFolder;
import hongik.map.honggildong.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkFolderRepository extends JpaRepository<BookmarkFolder,Long> {

    Optional<BookmarkFolder> findByIdAndMember(Long id, Member member);

    List<BookmarkFolder> findAllByMember(Member member);
}
