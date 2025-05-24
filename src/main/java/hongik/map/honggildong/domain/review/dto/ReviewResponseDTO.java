package hongik.map.honggildong.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewResponseDTO {
    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class General {
        private Long id;
    }

}
