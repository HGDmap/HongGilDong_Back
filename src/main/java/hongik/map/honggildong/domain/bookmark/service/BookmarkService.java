package hongik.map.honggildong.domain.bookmark.service;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.global.security.service.CustomUserDetails;

public interface BookmarkService {

    BookmarkResponseDTO.Single upsertBookmark(CustomUserDetails userDetails, Long folderId, Long facilityId);

    BookmarkResponseDTO.Single deleteBookmark(CustomUserDetails userDetails, Long facilityId);

    BookmarkResponseDTO.All allBookmark(CustomUserDetails userDetails);
}
