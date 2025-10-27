package hongik.map.honggildong.global.redis.search.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class SearchResultDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class AutoCompleteGeneral {
        private String name;
        private String type;
        private Long id;
    }


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class result{
        private Long id;
        private String name;
        private String type;
        private String description;
        private Double latitude;
        private Double longitude;
        private Long nodeId;
        @Builder.Default
        private Boolean isBookmarked = false;
        @Builder.Default
        @Setter
        private List<String> photoList = new ArrayList<String>();

        public void setIsBookmarkedTrue(){
            this.isBookmarked = true;
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class resultList{
        private Integer listSize;
        private List<result> resultList;
    }
}
