package hongik.map.honggildong.domain.bookmarkFolder.service;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.bookmark.repository.BookmarkRepository;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderRequestDTO;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderResponseDTO;
import hongik.map.honggildong.domain.bookmarkFolder.entity.BookmarkFolder;
import hongik.map.honggildong.domain.bookmarkFolder.repository.BookmarkFolderRepository;
import hongik.map.honggildong.domain.direction.dto.DirectionResponseDTO;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.member.repository.MemberRepository;
import hongik.map.honggildong.domain.node.entity.Node;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkFolderServiceImpl implements BookmarkFolderService {

    private final BookmarkFolderRepository bookmarkFolderRepository;
    private final MemberRepository memberRepository;
    private final FacilityRepository facilityRepository;
    private final BookmarkRepository bookmarkRepository;

    @Override
    public BookmarkResponseDTO.All createBookmarkFolder(CustomUserDetails userDetails,
                                                           BookmarkFolderRequestDTO.Create createFolderRequestDTO) {
        Member member = userDetails.getMember();
        
        BookmarkFolder bookmarkFolder = BookmarkFolder.builder()
                .name(createFolderRequestDTO.getFolderName())
                .color(createFolderRequestDTO.getFolderColor())
                .member(member)
                .build();

        bookmarkFolderRepository.save(bookmarkFolder);
        List<BookmarkFolder> bookmarkFolders = bookmarkFolderRepository.findAllByMember(member).stream().toList();

        return new BookmarkResponseDTO.All(getBookmarkFolderlist(bookmarkFolders));
    }

    @Override
    public BookmarkResponseDTO.All updateBookmarkFolder(Long folderId,
                                                           CustomUserDetails userDetails,
                                                           BookmarkFolderRequestDTO.Update updateFolderRequestDTO) {
        Member member = userDetails.getMember();

        BookmarkFolder bookmarkFolder = bookmarkFolderRepository.findByIdAndMember(folderId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_FOLDER_NOT_FOUND));

        bookmarkFolder.setName(updateFolderRequestDTO.getFolderName());
        bookmarkFolder.setColor(updateFolderRequestDTO.getFolderColor());

        List<BookmarkFolder> bookmarkFolders = bookmarkFolderRepository.findAllByMember(member).stream().toList();
        return new BookmarkResponseDTO.All(getBookmarkFolderlist(bookmarkFolders));
    }

    @Transactional
    @Override
    public void deleteBookmarkFolder(Long folderId, CustomUserDetails userDetails) {
        Member member = userDetails.getMember();

        BookmarkFolder folder = bookmarkFolderRepository.findById(folderId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_FOLDER_NOT_FOUND));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkFolder(folder);

        bookmarkRepository.deleteAll(bookmarks);
        bookmarkFolderRepository.delete(folder);

        return;
    }



    private List<BookmarkResponseDTO.Detail> getBookmarklist(BookmarkFolder bookmarkFolder) {

        // 즐겨찾기 폴더가 비어있을 때
        if (bookmarkFolder.getBookmarks() == null || bookmarkFolder.getBookmarks().isEmpty()) {
            return Collections.emptyList();
        }

        return bookmarkFolder.getBookmarks().stream()
                .map(f -> new BookmarkResponseDTO.Detail(
                        f.getFacility().getId(),
                        f.getFacility().getName(),
                        f.getFacility().getLocationDetail(),
                        f.getFacility().getOpenInfo(),
                        f.getFacility().getMainImg(),
                        f.getFacility().getBuilding().getLatitude(),
                        f.getFacility().getBuilding().getLongitude(),
                        f.getFacility().getBuilding().getId()
                ))
                .toList();
    }

    private List<BookmarkResponseDTO.Single> getBookmarkFolderlist(List<BookmarkFolder> bookmarkFolders) {

       return bookmarkFolders.stream()
                .map(f -> new BookmarkResponseDTO.Single(
                        f.getId(),
                        f.getName(),
                        f.getColor(),
                        getBookmarklist(f)
                ))
                .toList();
    }
}
