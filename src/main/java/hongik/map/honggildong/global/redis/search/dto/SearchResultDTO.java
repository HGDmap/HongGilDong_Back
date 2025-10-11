package hongik.map.honggildong.global.redis.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SearchResultDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class General{
        private String name;
        private String type;
        private Long id;
    }

    public static class GeneralList{
        private List<General> general;
    }
}
