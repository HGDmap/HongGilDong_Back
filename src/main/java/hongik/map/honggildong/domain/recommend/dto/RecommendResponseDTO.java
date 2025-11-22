package hongik.map.honggildong.domain.recommend.dto;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.facility.entity.HashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class RecommendResponseDTO {

    @Builder @Getter @AllArgsConstructor @NoArgsConstructor
    public static class General{
        List<FacilityList> recommendedFacilityList;
    }

    @Builder @Getter @AllArgsConstructor @NoArgsConstructor
    public static class FacilityList{
        HashTag hashTag;
        List<BookmarkResponseDTO.FacilityDetail> facilityList;
    }
}
