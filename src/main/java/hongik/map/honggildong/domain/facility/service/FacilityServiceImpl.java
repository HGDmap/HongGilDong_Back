package hongik.map.honggildong.domain.facility.service;

import hongik.map.honggildong.domain.bookmark.dto.JPQLBookmarkDTO;
import hongik.map.honggildong.domain.bookmark.entity.Bookmark;
import hongik.map.honggildong.domain.bookmark.repository.BookmarkRepository;
import hongik.map.honggildong.domain.facility.converter.FacilityConverter;
import hongik.map.honggildong.domain.facility.dto.FacilityResponseDTO;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.entity.FacilityType;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.review.repository.ReviewRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.redis.search.converter.SearchConverter;
import hongik.map.honggildong.global.redis.search.dto.SearchResultDTO;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {
    private final FacilityRepository facilityRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReviewRepository reviewRepository;


    @Override
    public Facility getFacilityById(Long facilityId) {

        return facilityRepository.findById(facilityId).orElseThrow(()->new GeneralException(ErrorStatus.FACILITY_NOT_FOUND));
    }

    @Override
    public FacilityResponseDTO.Detail getFacilityDetails(Long facilityId, CustomUserDetails userDetails) {

        Facility facility = getFacilityById(facilityId);


        //로그인 되어있지 않으면 항상 false
        Boolean isBookmarked = userDetails!=null ? bookmarkRepository.existsByMemberAndFacility(userDetails.getMember(), facility):false;

        List<String> photoList = new ArrayList<>();

        photoList.add(facility.getMainImg());
        photoList.add(facility.getMainImg2());
        photoList.add(facility.getMainImg3());

        return FacilityConverter.toDetailDTO(facility, isBookmarked, photoList);

    }

    @Override
    public FacilityResponseDTO.AvgRatingAndRecommendationStats getAvgRatings(Facility facility) {
        Double avgRating = reviewRepository.findAvgRatingByFacilityId(facility.getId());

        return FacilityConverter.toAvgRatingAndRecommendationStatsDTO(facility,avgRating);
    }

    @Override
    public SearchResultDTO.ResultList getFacilitiesByFacilityType(FacilityType type, CustomUserDetails userDetails) {

        List<Facility> facilities = facilityRepository.findAllByType(type);

        //interface(native query 프로젝션)->set
        List<Long> bookmarkedList;
        if(userDetails!=null){
            Member member = userDetails.getMember();
            bookmarkedList = bookmarkRepository.findAllFacilityIdByMemberAndFacilityIn(member,facilities);
        } else {
            bookmarkedList = new ArrayList<>();
        }

        List<SearchResultDTO.Result> resultList = facilities.stream().map(
                facility-> {
                    SearchResultDTO.Result result = SearchConverter.toResultDTO(facility);
                    if(bookmarkedList.contains(result.getId())){
                        result.setIsBookmarkedTrue();
                    }

                    return result;
                }
        ).toList();


        return SearchConverter.toResultListDTO(resultList);
    }
}
