package hongik.map.honggildong.domain.bookmark.service;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.bookmark.repository.BookmarkRepository;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderResponseDTO;
import hongik.map.honggildong.domain.bookmarkFolder.entity.BookmarkFolder;
import hongik.map.honggildong.domain.bookmarkFolder.repository.BookmarkFolderRepository;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkFolderRepository bookmarkFolderRepository;
    private final FacilityRepository facilityRepository;
    private final BookmarkRepository bookmarkRepository;

    // 즐겨찾기 수정 및 추가
    @Override
    public BookmarkResponseDTO.Single upsertBookmark(CustomUserDetails userDetails,
                                                     Long folderId,
                                                     Long facilityId) {

        Member member = userDetails.getMember();
        BookmarkFolder bookmarkFolder = bookmarkFolderRepository.findByIdAndMember(folderId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_FOLDER_NOT_FOUND));
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FACILITY_NOT_FOUND));

        Optional<Bookmark> preBookmark = bookmarkRepository.findByFacilityAndMember(facility, member);

        // 존재하면 수정, 존재하지 않으면 새롭게 추가
        if (preBookmark.isPresent()) {
            BookmarkFolder preBookmarkFolder = preBookmark.get().getBookmarkFolder();

            preBookmarkFolder.getBookmarks().remove(preBookmark.get());
            bookmarkRepository.deleteById(preBookmark.get().getId());

            Bookmark bookmark = Bookmark.builder()
                    .bookmarkFolder(bookmarkFolder)
                    .facility(facility)
                    .member(member)
                    .building(facility.getBuilding())
                    .build();

            bookmarkRepository.save(bookmark);
            bookmarkFolder.getBookmarks().add(bookmark);
            bookmarkRepository.save(bookmark);

            bookmarkFolder = preBookmarkFolder;
        } else {
            // 즐겨찾기 새롭게 추가
            Bookmark bookmark = Bookmark.builder()
                    .bookmarkFolder(bookmarkFolder)
                    .facility(facility)
                    .member(member)
                    .building(facility.getBuilding())
                    .build();

            bookmarkRepository.save(bookmark);
            bookmarkFolder.getBookmarks().add(bookmark);
            bookmarkRepository.save(bookmark);
        }

        return new BookmarkResponseDTO.Single(
                bookmarkFolder.getId(),
                bookmarkFolder.getName(),
                bookmarkFolder.getColor(),
                getBookmarklist(bookmarkFolder));
    }

    @Override
    @Transactional
    public BookmarkResponseDTO.Single deleteBookmark(CustomUserDetails userDetails,
                                                     Long facilityId) {

        Member member = userDetails.getMember();
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.FACILITY_NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByFacilityAndMember(facility, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_NOT_FOUND));

        BookmarkFolder bookmarkFolder = bookmark.getBookmarkFolder();

        bookmarkFolder.getBookmarks().remove(bookmark);
        bookmarkRepository.deleteById(bookmark.getId());

        return new BookmarkResponseDTO.Single(
                bookmarkFolder.getId(),
                bookmarkFolder.getName(),
                bookmarkFolder.getColor(),
                getBookmarklist(bookmarkFolder));
    }

    @Override
    public BookmarkResponseDTO.All allBookmark(CustomUserDetails userDetails) {

        Member member = userDetails.getMember();
        List<BookmarkFolder> bookmarkFolders = bookmarkFolderRepository.findAllByMember(member).stream().toList();

        List<BookmarkResponseDTO.Single> folderItems = bookmarkFolders.stream()
                .map(f -> new BookmarkResponseDTO.Single(
                        f.getId(),
                        f.getName(),
                        f.getColor(),
                        getBookmarklist(f)
                ))
                .toList();

        return new BookmarkResponseDTO.All(folderItems);
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


}
