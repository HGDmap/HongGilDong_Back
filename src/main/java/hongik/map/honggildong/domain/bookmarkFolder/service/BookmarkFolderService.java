package hongik.map.honggildong.domain.bookmarkFolder.service;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderRequestDTO;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderResponseDTO;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;

public interface BookmarkFolderService {

    BookmarkResponseDTO.All createBookmarkFolder(CustomUserDetails userDetails,
                                                    BookmarkFolderRequestDTO.Create createFolderRequestDTO);

    BookmarkResponseDTO.All updateBookmarkFolder(Long folderId,
                                                    CustomUserDetails userDetails,
                                                    BookmarkFolderRequestDTO.Update updateFolderRequestDTO);

    BookmarkResponseDTO.All deleteBookmarkFolder(Long folderId, CustomUserDetails userDetails);

    BookmarkResponseDTO.Single singleBookmarkFolder(Long folderId, CustomUserDetails userDetails);
}

