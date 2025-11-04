package hongik.map.honggildong.domain.bookmarkFolder.controller;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderRequestDTO;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderResponseDTO;
import hongik.map.honggildong.domain.bookmarkFolder.service.BookmarkFolderService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "즐겨찾기 폴더")
@RequestMapping("/bookmarks/folders")
public class BookmarkFolderController {
    private final BookmarkFolderService bookmarkFolderService;

    // 즐겨찾기 폴더 생성
    @PostMapping
    public ApiResponse<BookmarkResponseDTO.All> createFolder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @RequestBody BookmarkFolderRequestDTO.Create createFolderRequestDTO) {

        BookmarkResponseDTO.All body = bookmarkFolderService.createBookmarkFolder(userDetails, createFolderRequestDTO);
        return ApiResponse.onSuccess(body);
    }

    // 즐겨찾기 폴더 삭제
    @DeleteMapping("/{folderId}")
    public ApiResponse<?> deleteFolder(@PathVariable Long folderId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        bookmarkFolderService.deleteBookmarkFolder(folderId, userDetails);
        return ApiResponse.onSuccess("해당 즐겨찾기 폴더가 삭제되었습니다.");
    }


    // 즐겨찾기 폴더 수정
    @PutMapping("/{folderId}")
    public ApiResponse<BookmarkResponseDTO.All> updateFolder(@PathVariable Long folderId,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @RequestBody BookmarkFolderRequestDTO.Update updateFolderRequestDTO) {

        BookmarkResponseDTO.All body = bookmarkFolderService.updateBookmarkFolder(folderId, userDetails, updateFolderRequestDTO);
        return ApiResponse.onSuccess(body);
    }

}
