package hongik.map.honggildong.domain.bookmarkFolder.service;

import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderRequestDTO;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface BookmarkFolderService {

    BookmarkFolderResponseDTO.Single createBookmarkFolder(UserDetails userDetails,
                                                          BookmarkFolderRequestDTO.Create createFolderRequestDTO);

    BookmarkFolderResponseDTO.Single updateBookmarkFolder(Long folderId,
                                                          UserDetails userDetails,
                                                          BookmarkFolderRequestDTO.Update updateFolderRequestDTO);

    BookmarkFolderResponseDTO.Single addBookmark(UserDetails userDetails,
                                                 Long facilityId,
                                                 Long folderId);

    BookmarkFolderResponseDTO.Single getBookmarks(Long folderId,
                                                  UserDetails userDetails);

    void deleteBookmarkFolder(Long folderId, UserDetails userDetails);
    void deleteBookmark(Long facilityId, UserDetails userDetails);
}

