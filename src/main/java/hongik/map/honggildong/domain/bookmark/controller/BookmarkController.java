package hongik.map.honggildong.domain.bookmark.controller;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.bookmark.repository.BookmarkRepository;
import hongik.map.honggildong.domain.bookmark.service.BookmarkService;
import hongik.map.honggildong.domain.event.dto.EventResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Member;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
@Tag(name = "즐겨찾기")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @DeleteMapping("{bookmarkId}")
    public final ApiResponse<Void> deleteBookmark(@PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmark(Long bookmarkId);
        return ApiResponse.onSuccess();
    }

    @GetMapping("/all")
    public final ApiResponse<BookmarkResponseDTO.AllBookmarks> getBookmarkList(
            @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.getMemberByUserDetails(userDetails);
        BookmarkResponseDTO.AllBookmarks allBookmarks = new BookmarkResponseDTO.AllBookmarks(member);
        return ApiResponse.onSuccess(allBookmarks);
    }
}
