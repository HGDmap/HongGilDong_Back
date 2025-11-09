package hongik.map.honggildong.domain.review.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewRequestDTO {

    @Getter
    public static class create{
        private String content;
        private List<String> photoList;
    }
}
