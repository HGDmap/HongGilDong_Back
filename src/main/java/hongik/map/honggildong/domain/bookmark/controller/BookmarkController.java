package hongik.map.honggildong.domain.bookmark.controller;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.bookmark.service.BookmarkService;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderResponseDTO;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "즐겨찾기")
@RequestMapping("/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 즐겨찾기 추가 및 수정
    @PostMapping
    public ApiResponse<BookmarkResponseDTO.Single> addBookmark(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @RequestParam Long folderId,
                                                               @RequestParam Long facilityId) {

        BookmarkResponseDTO.Single body = bookmarkService.upsertBookmark(userDetails, folderId, facilityId);
        return ApiResponse.onSuccess(body);

    }

    // 즐겨찾기 삭제
    @DeleteMapping("/{facilityId}")
    public ApiResponse<BookmarkResponseDTO.Single> deleteBookmark(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @RequestParam Long facilityId) {

        BookmarkResponseDTO.Single body = bookmarkService.deleteBookmark(userDetails, facilityId);
        return ApiResponse.onSuccess(body);

    }

    // 전체 즐겨찾기 조회
    @PostMapping("/all")
    public ApiResponse<BookmarkResponseDTO.All> allBookmarkList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        BookmarkResponseDTO.All body = bookmarkService.allBookmark(userDetails);
        return ApiResponse.onSuccess(body);
    }


}
