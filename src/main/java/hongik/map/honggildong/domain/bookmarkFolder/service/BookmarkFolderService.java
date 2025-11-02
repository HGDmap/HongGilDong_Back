package hongik.map.honggildong.domain.bookmarkFolder.service;

import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderRequestDTO;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderResponseDTO;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

public interface BookmarkFolderService {

    BookmarkFolderResponseDTO.Single createBookmarkFolder(CustomUserDetails userDetails,
                                                          BookmarkFolderRequestDTO.Create createFolderRequestDTO);

    BookmarkFolderResponseDTO.Single updateBookmarkFolder(Long folderId,
                                                          CustomUserDetails userDetails,
                                                          BookmarkFolderRequestDTO.Update updateFolderRequestDTO);

    BookmarkFolderResponseDTO.Single addBookmark(CustomUserDetails userDetails,
                                                 Long facilityId,
                                                 Long folderId);

    BookmarkFolderResponseDTO.Single getBookmarks(Long folderId,
                                                  CustomUserDetails userDetails);

    void deleteBookmarkFolder(Long folderId, CustomUserDetails userDetails);
    void deleteBookmark(Long facilityId, CustomUserDetails userDetails);
}

