package hongik.map.honggildong.domain.bookmark.service;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.bookmark.entity.BookmarkType;
import hongik.map.honggildong.domain.bookmark.repository.BookmarkRepository;
import hongik.map.honggildong.domain.bookmarkFolder.entity.BookmarkFolder;
import hongik.map.honggildong.domain.bookmarkFolder.repository.BookmarkFolderRepository;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.building.repository.BuildingRepository;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkFolderRepository bookmarkFolderRepository;
    private final FacilityRepository facilityRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BuildingRepository buildingRepository;

    // 즐겨찾기 수정 및 추가
    @Override
    @Transactional
    public BookmarkResponseDTO.Single upsertBookmark(CustomUserDetails userDetails,
                                                     Long folderId,
                                                     Long targetId,
                                                     BookmarkType type) {

        Member member = userDetails.getMember();
        BookmarkFolder bookmarkFolder = bookmarkFolderRepository.findByIdAndMember(folderId, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_FOLDER_NOT_FOUND));

        if (type == BookmarkType.FACILITY) {
            Facility facility = facilityRepository.findById(targetId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.FACILITY_NOT_FOUND));
            Optional<Bookmark> preBookmark = bookmarkRepository.findByFacilityAndMember(facility, member);

            if (preBookmark.isPresent()) {
                bookmarkFolder = moveFolder(preBookmark.get(), bookmarkFolder);
            } else {
                Bookmark bookmark = Bookmark.builder()
                                    .bookmarkFolder(bookmarkFolder)
                                    .member(member)
                                    .facility(facility)
                                    .building(null)
                                    .type(BookmarkType.FACILITY)
                                    .build();

                bookmarkRepository.save(bookmark);
                bookmarkFolder.getBookmarks().add(bookmark);
            }
        }

        if (type == BookmarkType.BUILDING) {
            Building building = buildingRepository.findById(targetId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.BUILDING_NOT_FOUND));
            Optional<Bookmark> preBookmark = bookmarkRepository.findByBuildingAndMember(building, member);

            if (preBookmark.isPresent()) {
                bookmarkFolder = moveFolder(preBookmark.get(), bookmarkFolder);
            } else {
                Bookmark bookmark = Bookmark.builder()
                        .bookmarkFolder(bookmarkFolder)
                        .member(member)
                        .facility(null)
                        .building(building)
                        .type(BookmarkType.BUILDING)
                        .build();

                bookmarkRepository.save(bookmark);
                bookmarkFolder.getBookmarks().add(bookmark);
            }
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
                                                     Long targetId,
                                                     BookmarkType bookmarkType) {

        Member member = userDetails.getMember();

        Bookmark bookmark = null;

        if (bookmarkType == BookmarkType.FACILITY) {
            Facility facility = facilityRepository.findById(targetId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.FACILITY_NOT_FOUND));

            bookmark = bookmarkRepository.findByFacilityAndMember(facility, member)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_NOT_FOUND));

        } else if (bookmarkType == BookmarkType.BUILDING) {
            Building building = buildingRepository.findById(targetId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.BUILDING_NOT_FOUND));

            bookmark = bookmarkRepository.findByBuildingAndMember(building, member)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_NOT_FOUND));
        }

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


    // 즐겨찾기 수정시 즐겨찾기 폴더 이동
    private BookmarkFolder moveFolder(Bookmark bookmark, BookmarkFolder targetFolder) {
        BookmarkFolder prev = bookmark.getBookmarkFolder();

        if (!prev.getId().equals(targetFolder.getId())) {
            bookmark.setBookmarkFolder(targetFolder);
            prev.getBookmarks().remove(bookmark);

            targetFolder.getBookmarks().add(bookmark);
        }

        return prev;
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

}
