package hongik.map.honggildong.domain.bookmark.service;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkRequestDTO;
import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public class BookmarkGroupServiceImpl extends BookmarkService {

    @Override
    public BookmarkResponseDTO.FolderData getBookmarkGroup(Member member) {
        return null;
    }

    @Override
    public BookmarkResponseDTO.FolderDetail getBookmarkGroupDetail(
            Member member,
            long folderId) {
        return null;
    }

    @Override
    public void deleteBookmarkGroup(
            Member member,
            long folderId) {
    }

    @Override
    public BookmarkResponseDTO.FolderData postBookmarkGroup(
            Member member,
            BookmarkRequestDTO.General bookmarkGroupInfo) {
        return null;
    }

    @Override
    public BookmarkResponseDTO.FolderData modifyBookmarkGroup(
            Member member,
            BookmarkRequestDTO.General bookmarkGroupInfo) {
        return null;
    }



}