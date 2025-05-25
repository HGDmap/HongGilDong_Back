package hongik.map.honggildong.domain.bookmark.controller;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkRequestDTO;
import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmark.service.BookmarkGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks/folders")
@Tag(name = "즐겨찾기 그룹")
public class BookmarkGroupController {

    private final BookmarkGroupService bookmarkGroupService;

    @GetMapping
    public final ApiResponse<BookmarkResponseDTO.FolderData> getBookmarkGroup(
            @AuthenticationPrincipal UserDetails userDetails) {
        BookmarkResponseDTO.FolderData folderData = new BookmarkResponseDTO.FolderData();
        folderData = bookmarkGroupService.getBookmarkGroup(userDetails);
        return ApiResponse.onSuccess(folderData);
    }

    @PostMapping
    public final ApiResponse<BookmarkResponseDTO.FolderData> postBookmarkGroup(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BookmarkRequestDTO.General bookmarkinfo) {
        BookmarkResponseDTO.FolderData folderData = new BookmarkResponseDTO.FolderData();
        folderData = bookmarkGroupService.postBookmarkGroup(userDetails, bookmarkinfo);
        return ApiResponse.onSuccess(folderData);
    }

    @PutMapping("{folderId}")
    public final ApiResponse<BookmarkResponseDTO.FolderData> putBookmarkGroup(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody BookmarkRequestDTO.General bookmarkGroupInfo,
            @PathVariable Long folderId) {
        BookmarkResponseDTO.FolderData folderData = new BookmarkResponseDTO.FolderData();
        folderData = bookmarkGroupService.modifyBookmarkGroup(userDetails, bookmarkGroupInfo);
        return ApiResponse.onSuccess(allBookmarks);
    }

    @GetMapping("{folderId}")
    public final ApiResponse<BookmarkResponseDTO.FolderDetail> getBookmarkGroupDetail(
            @PathVariable Long folderId,
            @AuthenticationPrincipal UserDetails userDetails) {
        BookmarkResponseDTO.FolderDetail folderDetail = new BookmarkResponseDTO.FolderDetail();
        folderDetail = bookmarkGroupService.getBookmarkGroupDetail(member, folderId);
        return ApiResponse.onSuccess(folderDetail);
    }

    @DeleteMapping("{folderId}")
    public final ApiResponse<void> deleteBookmarkGroup(
            @PathVariable Long folderId,
            @AuthenticationPrincipal UserDetails userDetails) {
        bookmarkGroupService.deleteBookmarkGroup(member, folderId);
        return ApiResponse.onSuccess();
    }

}