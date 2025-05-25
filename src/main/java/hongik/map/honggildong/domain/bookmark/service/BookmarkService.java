package hongik.map.honggildong.domain.bookmark.service;


import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;

public interface BookmarkService {

    public void bookmarkDelete(Long bookmarkId);

    public void bookmarkUpdate(Long bookmarkId);

    public BookmarkResponseDTO.AllBookmarks getAllBookmarks(Member member);
}
