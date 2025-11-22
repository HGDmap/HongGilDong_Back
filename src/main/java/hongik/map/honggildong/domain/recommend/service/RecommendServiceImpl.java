package hongik.map.honggildong.domain.recommend.service;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.bookmark.entity.BookmarkType;
import hongik.map.honggildong.domain.bookmarkFolder.entity.BookmarkFolder;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.entity.HashTag;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.domain.recommend.dto.RecommendResponseDTO;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {

    private final FacilityRepository facilityRepository;

    private static final Map<HashTag, String> SORT_FIELD_BY_TAG = Map.of(
            HashTag.FOOD, "foodCnt",
            HashTag.REST, "restCnt",
            HashTag.STUDY, "studyCnt",
            HashTag.MEETING, "meetingCnt",
            HashTag.VIEW, "viewCnt"
    );

    private static final List<HashTag> RECOMMEND_TAGS = List.of(
            HashTag.FOOD,
            HashTag.REST,
            HashTag.STUDY,
            HashTag.MEETING,
            HashTag.VIEW
    );


    public RecommendResponseDTO.General getRecommendation() {

        List<RecommendResponseDTO.FacilityList> lists =
                RECOMMEND_TAGS.stream()
                        .map(this::buildFacilityListForTag)
                        .filter(fl -> fl.getFacilityList() != null && !fl.getFacilityList().isEmpty())
                        .toList();

        return RecommendResponseDTO.General.builder()
                .recommendedFacilityList(lists)
                .build();
    }


    private RecommendResponseDTO.FacilityList buildFacilityListForTag(HashTag tag) {
        String sortField = SORT_FIELD_BY_TAG.get(tag);

        Slice<Facility> page = facilityRepository.findAll(
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, sortField))
        );

        List<BookmarkResponseDTO.FacilityDetail> facilities =
                page.getContent().stream()
                        .map(this::toFacilityDetail)
                        .toList();

        return RecommendResponseDTO.FacilityList.builder()
                .hashTag(tag)
                .facilityList(facilities)
                .build();
    }

    // dto 구현 부분
    private BookmarkResponseDTO.FacilityDetail toFacilityDetail(Facility fac) {
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

}
