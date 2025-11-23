package hongik.map.honggildong.domain.facility.dto;

import hongik.map.honggildong.domain.facility.entity.Facility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FacilityResponseDTO {
    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AvgRatingAndRecommendationStats {
        private Double avgRating;
        private RecommendationStats recommendation;
    }

    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendationStats {
        private Long meetingCnt;
        private Long studyCnt;
        private Long restCnt;
        private Long viewCnt;
        private Long foodCnt;
    }

    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        private Long id;
        private String name;
        private String type;
        private Long nodeId;
        private String nodeName;
        private String open;
        private String phone;
        private String link;
        private String description;
        private Double latitude;
        private Double longitude;
        private List<String> photoList;
        private Boolean isBookmarked;
    }

    //추후에 bookmark dto 로 옮기는 것 고려
    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookmarkStatus {
        private Long facilityId;
        private Boolean isBookMarked;
    }
}
