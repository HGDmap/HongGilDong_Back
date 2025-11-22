package hongik.map.honggildong.domain.review.dto;

import hongik.map.honggildong.domain.facility.entity.HashTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class ReviewRequestDTO {

    @Getter
    public static class create{
        @Schema(example = "4.5")
        private Double rating;
        private HashTag recommend;
        private String content;
        private List<String> photoList;
    }
}
