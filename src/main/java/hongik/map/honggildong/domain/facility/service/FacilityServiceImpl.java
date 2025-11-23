package hongik.map.honggildong.domain.facility.service;

import hongik.map.honggildong.domain.bookmark.repository.BookmarkRepository;
import hongik.map.honggildong.domain.facility.converter.FacilityConverter;
import hongik.map.honggildong.domain.facility.dto.FacilityResponseDTO;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.repository.FacilityRepository;
import hongik.map.honggildong.domain.review.repository.ReviewRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
}
