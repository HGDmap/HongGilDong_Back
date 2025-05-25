package hongik.map.honggildong.domain.bookmark.service;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkRequestDTO;
import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;

public interface BookmarkGroupService {

    public BookmarkResponseDTO.FolderData getBookmarkGroup(Member member);

    public BookmarkResponseDTO.FolderDetail getBookmarkGroupDetail(
            Member member,
            long folderId);

    public void deleteBookmarkGroup(
            Member member,
            long folderId);

    public BookmarkResponseDTO.FolderData postBookmarkGroup(
            Member member,
            BookmarkRequestDTO.General bookmarkGroupInfo);

    public BookmarkResponseDTO.FolderData modifyBookmarkGroup(
            Member member,
            BookmarkRequestDTO.General bookmarkGroupInfo);
}
