package hongik.map.honggildong.domain.facility.service;

import hongik.map.honggildong.domain.facility.dto.FacilityResponseDTO;
import hongik.map.honggildong.domain.facility.entity.Facility;
import hongik.map.honggildong.domain.facility.entity.FacilityType;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.redis.search.dto.SearchResultDTO;
import hongik.map.honggildong.global.security.service.CustomUserDetails;

public interface FacilityService {
    Facility getFacilityById(Long facilityId);

    FacilityResponseDTO.Detail getFacilityDetails(Long facilityId, CustomUserDetails userDetails);

    FacilityResponseDTO.AvgRatingAndRecommendationStats getAvgRatings(Facility facility);

    SearchResultDTO.ResultList getFacilitiesByFacilityType(FacilityType type, CustomUserDetails userDetails);
}
