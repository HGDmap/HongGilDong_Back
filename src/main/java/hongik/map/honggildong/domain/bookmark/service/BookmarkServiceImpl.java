package hongik.map.honggildong.domain.bookmark.service;
import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import org.springframework.stereotype.Service;

@Service
public class BookmarkServiceImpl extends BookmarkService {

    @Override
    public void bookmarkDelete(Long bookmarkId){
    }

    @Override
    public void bookmarkUpdate(Long bookmarkId){
    }

    @Override
    public BookmarkResponseDTO.AllBookmarks getAllBookmarks(Member member){
        return null;
    }

}
