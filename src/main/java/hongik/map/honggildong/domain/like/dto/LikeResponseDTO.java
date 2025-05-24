package hongik.map.honggildong.domain.like.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LikeResponseDTO {
    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class General {
        private Long id;
    }
}
