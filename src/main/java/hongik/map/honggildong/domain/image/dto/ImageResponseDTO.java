package hongik.map.honggildong.domain.image.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ImageResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PresignedDTO{
        private String presignedURL;
        private String imageURL;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ImagePage{
        private List<String> imageList;
        private int pageSize;
        private Boolean isFirst;
        private Boolean isLast;
        @Nullable
        private String continuationToken;
    }
}
