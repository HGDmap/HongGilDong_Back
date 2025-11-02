package hongik.map.honggildong.domain.bookmarkFolder.controller;

import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderRequestDTO;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderResponseDTO;
import hongik.map.honggildong.domain.bookmarkFolder.service.BookmarkFolderService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks/folders")
public class BookmarkFolderController {
    private final BookmarkFolderService bookmarkFolderService;

    @PostMapping
    public ApiResponse<BookmarkFolderResponseDTO.Single> createFolder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @RequestBody BookmarkFolderRequestDTO.Create createFolderRequestDTO) {
        BookmarkFolderResponseDTO.Single body = bookmarkFolderService.createBookmarkFolder(userDetails, createFolderRequestDTO);
        return ApiResponse.onSuccess(body);
    }

    @DeleteMapping("/{folderId}")
    public ApiResponse<?> deleteFolder(@PathVariable Long folderId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        bookmarkFolderService.deleteBookmarkFolder(folderId, userDetails);
        return ApiResponse.onSuccess("해당 즐겨찾기 폴더가 삭제되었습니다.");
    }

    @DeleteMapping("/bookmarks/{facilityId}")
    public ApiResponse<?> deleteBookmark(
            @PathVariable Long facilityId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        bookmarkFolderService.deleteBookmark(facilityId, userDetails);
        return ApiResponse.onSuccess("해당 시설의 즐겨찾기가 삭제되었습니다.");
    }

    @PutMapping("/{folderId}")
    public ApiResponse<BookmarkFolderResponseDTO.Single> updateFolder(@PathVariable Long folderId,
                                                                      @AuthenticationPrincipal UserDetails userDetails,
                                                                      @RequestBody BookmarkFolderRequestDTO.Update updateFolderRequestDTO) {
        BookmarkFolderResponseDTO.Single body = bookmarkFolderService.updateBookmarkFolder(folderId, userDetails, updateFolderRequestDTO);
        return ApiResponse.onSuccess(body);
    }

    @PostMapping("/{folderId}/bookmarks/{facilityId}")
    public ApiResponse<BookmarkFolderResponseDTO.Single> addBookmark(@AuthenticationPrincipal UserDetails userDetails,
                                                                     @PathVariable Long folderId,
                                                                     @PathVariable Long facilityId) {
        BookmarkFolderResponseDTO.Single body = bookmarkFolderService.addBookmark(userDetails, folderId, facilityId);
        return ApiResponse.onSuccess(body);
    }

    @GetMapping("/{folderId}")
    public ApiResponse<BookmarkFolderResponseDTO.Single> updateFolder(@PathVariable Long folderId,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        BookmarkFolderResponseDTO.Single body = bookmarkFolderService.getBookmarks(folderId, userDetails);
        return ApiResponse.onSuccess(body);
    }


}
