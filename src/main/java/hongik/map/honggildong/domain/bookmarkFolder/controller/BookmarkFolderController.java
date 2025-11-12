package hongik.map.honggildong.domain.bookmarkFolder.controller;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderRequestDTO;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderResponseDTO;
import hongik.map.honggildong.domain.bookmarkFolder.service.BookmarkFolderService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "즐겨찾기 폴더 생성")
    public ApiResponse<BookmarkResponseDTO.All> createFolder(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @RequestBody BookmarkFolderRequestDTO.Create createFolderRequestDTO) {

        BookmarkResponseDTO.All body = bookmarkFolderService.createBookmarkFolder(userDetails, createFolderRequestDTO);
        return ApiResponse.onSuccess(body);
    }

    // 단일 즐겨찾기 폴더 내 즐겨찾기 목록 조회
    @GetMapping("/{folderId}")
    @Operation(summary = "단일 즐겨찾기 폴더 내에 속한 북마크 목록 조회")
    public ApiResponse<BookmarkResponseDTO.Single> getBookmarkList(@PathVariable("folderId") Long folderId,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {

        BookmarkResponseDTO.Single body = bookmarkFolderService.singleBookmarkFolder(folderId, userDetails);
        return ApiResponse.onSuccess(body);
    }

    // 즐겨찾기 폴더 삭제
    @DeleteMapping("/{folderId}")
    @Operation(summary = "폴더 삭제, 내부 모든 북마크도 함께 삭제")
    public ApiResponse<BookmarkResponseDTO.All> deleteFolder(@PathVariable("folderId") Long folderId,
                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        BookmarkResponseDTO.All body = bookmarkFolderService.deleteBookmarkFolder(folderId, userDetails);
        return ApiResponse.onSuccess(body);
    }


    // 즐겨찾기 폴더 수정
    @PutMapping("/{folderId}")
    @Operation(summary = "북마크 폴더 수정")
    public ApiResponse<BookmarkResponseDTO.All> updateFolder(@PathVariable("folderId") Long folderId,
                                                                      @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @RequestBody BookmarkFolderRequestDTO.Update updateFolderRequestDTO) {

        BookmarkResponseDTO.All body = bookmarkFolderService.updateBookmarkFolder(folderId, userDetails, updateFolderRequestDTO);
        return ApiResponse.onSuccess(body);
    }

}
