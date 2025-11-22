package hongik.map.honggildong.domain.likes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LikeResponseDTO {
    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class General {
        private Boolean isLiked;
        private Long reviewLikedCnt;
    }

    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GeneralPage {
        private Boolean isLiked;
        private Long reviewLikedCnt;
    }
}
