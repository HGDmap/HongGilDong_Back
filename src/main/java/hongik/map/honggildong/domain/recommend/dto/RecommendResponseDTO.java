package hongik.map.honggildong.domain.recommend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.facility.entity.HashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class RecommendResponseDTO {

    @Builder @Getter @AllArgsConstructor @NoArgsConstructor
    public static class General{
        List<FacilityList> recommendedFacilityList;
    }

    @Builder @Getter @AllArgsConstructor @NoArgsConstructor
    public static class FacilityList{
        HashTag hashTag;
        List<FacilityDetail> facilityList;
    }

    @Builder @Getter @NoArgsConstructor @AllArgsConstructor
    public static class FacilityDetail {
        private Long id;
        private String name;
        private String location;

        @JsonIgnore
        private String openInfo;

        private List<String> images;
        private Double latitude;
        private Double longitude;
        private Long nodeId;

        private boolean isBookmarked;
    }
}
