package hongik.map.honggildong.domain.bookmarkFolder.service;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.bookmark.entity.BookmarkType;
import hongik.map.honggildong.domain.bookmark.repository.BookmarkRepository;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderRequestDTO;
import hongik.map.honggildong.domain.bookmarkFolder.entity.BookmarkFolder;
import hongik.map.honggildong.domain.bookmarkFolder.repository.BookmarkFolderRepository;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.member.repository.MemberRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
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
    public BookmarkResponseDTO.Single singleBookmarkFolder(Long folderId, CustomUserDetails userDetails) {
        Member member = userDetails.getMember();

        BookmarkFolder folder = bookmarkFolderRepository.findById(folderId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_FOLDER_NOT_FOUND));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkFolder(folder);

        return new BookmarkResponseDTO.Single(
                folder.getId(),
                folder.getName(),
                folder.getColor(),
                getBookmarklist(folder));
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

    @Override
    public BookmarkResponseDTO.All deleteBookmarkFolder(Long folderId, CustomUserDetails userDetails) {
        Member member = userDetails.getMember();

        BookmarkFolder folder = bookmarkFolderRepository.findById(folderId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_FOLDER_NOT_FOUND));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkFolder(folder);

        bookmarkRepository.deleteAll(bookmarks);
        bookmarkFolderRepository.delete(folder);

        List<BookmarkFolder> bookmarkFolders = bookmarkFolderRepository.findAllByMember(member).stream().toList();
        return new BookmarkResponseDTO.All(getBookmarkFolderlist(bookmarkFolders));
    }



    // 즐겨찾기 목록 dto 구현 부분
    private List<BookmarkResponseDTO.Detail> getBookmarklist(BookmarkFolder bookmarkFolder) {
        List<Bookmark> bookmarks = bookmarkFolder.getBookmarks();

        if (bookmarks == null || bookmarks.isEmpty()) {
            return Collections.emptyList();
        }

        return bookmarks.stream()
                .map(bm -> {
                    if (bm.getType() == BookmarkType.FACILITY) {
                        Facility fac = bm.getFacility();
                        Building b = fac.getBuilding();
                        List<String> images = Stream.of(
                                        fac.getMainImg(),
                                        fac.getMainImg2(),
                                        fac.getMainImg3()
                                )
                                .filter(img -> img != null && !img.isBlank())
                                .toList();

                        return BookmarkResponseDTO.FacilityDetail.builder()
                                .id(fac.getId())
                                .name(fac.getName())
                                .location(fac.getNode().getName())
                                .openInfo(fac.getOpenInfo())
                                .images(images)
                                .latitude(b.getLatitude())
                                .longitude(b.getLongitude())
                                .nodeId(fac.getNode().getId())
                                .build();
                    }

                    if (bm.getType() == BookmarkType.BUILDING) {
                        Building b = bm.getBuilding();
                        List<String> images = Stream.of(b.getMainImg())
                                .filter(img -> img != null && !img.isBlank())
                                .toList();

                        return BookmarkResponseDTO.BuildingDetail.builder()
                                .id(b.getId())
                                .name(b.getName())
                                .images(images)
                                .latitude(b.getLatitude())
                                .longitude(b.getLongitude())
                                .nodeId(b.getMainNode().getId())
                                .build();
                    }

                    throw new GeneralException(ErrorStatus.BOOKMARK_NOT_FOUND);
                })
                .map(BookmarkResponseDTO.Detail.class::cast)
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
