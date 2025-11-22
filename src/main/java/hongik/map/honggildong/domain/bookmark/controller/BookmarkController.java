package hongik.map.honggildong.domain.bookmark.controller;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmark.entity.BookmarkType;
import hongik.map.honggildong.domain.bookmark.service.BookmarkService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "즐겨찾기")
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 즐겨찾기 추가 및 수정
    @PostMapping("/{type}")
    @Operation(summary = "즐겨찾기 추가 및 수정")
    public ApiResponse<BookmarkResponseDTO.Single> addBookmark(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable("type") BookmarkType type,
                                                               @RequestParam("folderId") Long folderId,
                                                               @RequestParam("targetId") Long targetId) {

        BookmarkResponseDTO.Single body = bookmarkService.upsertBookmark(userDetails, folderId, targetId, type);
        return ApiResponse.onSuccess(body);

    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{type}")
    @Operation(summary = "즐겨찾기 삭제")
    public ApiResponse<BookmarkResponseDTO.Single> deleteBookmark(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @PathVariable("type") BookmarkType type,
                                                                  @RequestParam("targetId") Long targetId) {

        BookmarkResponseDTO.Single body = bookmarkService.deleteBookmark(userDetails, targetId, type);
        return ApiResponse.onSuccess(body);

    }


    // 전체 즐겨찾기 조회
    @GetMapping("/all")
    @Operation(summary = "전체 즐겨찾기 조회")
    public ApiResponse<BookmarkResponseDTO.All> allBookmarkList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        BookmarkResponseDTO.All body = bookmarkService.allBookmark(userDetails);
        return ApiResponse.onSuccess(body);
    }


}
