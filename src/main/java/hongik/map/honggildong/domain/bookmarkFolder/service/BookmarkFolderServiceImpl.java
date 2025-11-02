package hongik.map.honggildong.domain.bookmarkFolder.service;

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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookmarkFolderServiceImpl implements BookmarkFolderService {

    private final BookmarkFolderRepository bookmarkFolderRepository;
    private final MemberRepository memberRepository;
    private final FacilityRepository facilityRepository;

    @Override
    public BookmarkFolderResponseDTO.Single createBookmarkFolder(CustomUserDetails userDetails,
                                                                 BookmarkFolderRequestDTO.Create createFolderRequestDTO) {
        Member member = userDetails.getMember();
        
        BookmarkFolder bookmarkFolder = BookmarkFolder.builder()
                .name(createFolderRequestDTO.getFolderName())
                .color(createFolderRequestDTO.getFolderColor())
                .member(member)
                .build();

        bookmarkFolderRepository.save(bookmarkFolder);
        return new BookmarkFolderResponseDTO.Single(
                bookmarkFolder.getId(),
                bookmarkFolder.getName(),
                bookmarkFolder.getColor(),
                Collections.emptyList() // 처음엔 비어 있음
        );
    }

    @Override
    public BookmarkFolderResponseDTO.Single updateBookmarkFolder(Long folderId,
                                                                 CustomUserDetails userDetails,
                                                                 BookmarkFolderRequestDTO.Update updateFolderRequestDTO) {
        Member member = userDetails.getMember();

        BookmarkFolder bookmarkFolder = bookmarkFolderRepository.findById(folderId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_FOLDER_NOT_FOUND));

        List<Facility> facilities = facilityRepository.findAllByBookmarkFolder(bookmarkFolder);

        bookmarkFolder.setName(updateFolderRequestDTO.getFolderName());
        bookmarkFolder.setColor(updateFolderRequestDTO.getFolderColor());

        List<BookmarkFolderResponseDTO.BookmarkList> items = facilities.stream()
                .map(f -> new BookmarkFolderResponseDTO.BookmarkList(
                        f.getId(),
                        f.getNode().getLatitude(),
                        f.getNode().getLongitude()
                ))
                .toList();

        return new BookmarkFolderResponseDTO.Single(
                bookmarkFolder.getId(),
                bookmarkFolder.getName(),
                bookmarkFolder.getColor(),
                items
        );
    }

    @Transactional
    @Override
    public void deleteBookmarkFolder(Long folderId, CustomUserDetails userDetails) {
        Member member = userDetails.getMember();

        BookmarkFolder folder = bookmarkFolderRepository.findById(folderId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_FOLDER_NOT_FOUND));

        List<Facility> facilities = facilityRepository.findAllByBookmarkFolder(folder);
        facilities.forEach(f -> f.setBookmarkFolder(null));

        bookmarkFolderRepository.delete(folder);
    }

    @Transactional
    @Override
    public void deleteBookmark(Long facilityId, CustomUserDetails userDetails) {
        Member member = userDetails.getMember();

        Facility facility = facilityRepository.findById(facilityId).orElseThrow(() -> new GeneralException(ErrorStatus.FACILITY_NOT_FOUND));

        facility.setBookmarkFolder(null);
    }

    @Override
    public BookmarkFolderResponseDTO.Single addBookmark(CustomUserDetails userDetails,
                                                         Long folderId,
                                                         Long facilityId) {
        Member member = userDetails.getMember();
        BookmarkFolder bookmarkFolder = bookmarkFolderRepository.findById(folderId).orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_FOLDER_NOT_FOUND));
        Facility facility = facilityRepository.findById(facilityId).orElseThrow(() -> new GeneralException(ErrorStatus.FACILITY_NOT_FOUND));

        facility.setBookmarkFolder(bookmarkFolder);
        facilityRepository.save(facility);

        List<Facility> facilities = facilityRepository.findAllByBookmarkFolder(bookmarkFolder);

        List<BookmarkFolderResponseDTO.BookmarkList> items = facilities.stream()
                .map(f -> new BookmarkFolderResponseDTO.BookmarkList(
                        f.getId(),
                        f.getNode().getLatitude(),
                        f.getNode().getLongitude()
                ))
                .toList();

        return new BookmarkFolderResponseDTO.Single(
                bookmarkFolder.getId(),
                bookmarkFolder.getName(),
                bookmarkFolder.getColor(),
                items
        );
    }

    @Override
    public BookmarkFolderResponseDTO.Single getBookmarks(Long folderId,
                                                         UserDetails userDetails) {
        Member member = userDetails.getMember();
        BookmarkFolder bookmarkFolder = bookmarkFolderRepository.findById(folderId).orElseThrow(() -> new GeneralException(ErrorStatus.BOOKMARK_FOLDER_NOT_FOUND));

        List<Facility> facilities = facilityRepository.findAllByBookmarkFolder(bookmarkFolder);

        List<BookmarkFolderResponseDTO.BookmarkList> items = facilities.stream()
                .map(f -> new BookmarkFolderResponseDTO.BookmarkList(
                        f.getId(),
                        f.getNode().getLatitude(),
                        f.getNode().getLongitude()
                ))
                .toList();

        return new BookmarkFolderResponseDTO.Single(
                bookmarkFolder.getId(),
                bookmarkFolder.getName(),
                bookmarkFolder.getColor(),
                items
        );
    }
}
