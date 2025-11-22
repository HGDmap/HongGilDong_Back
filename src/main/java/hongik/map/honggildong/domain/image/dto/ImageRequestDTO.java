package hongik.map.honggildong.domain.image.dto;

import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.List;

public class ImageRequestDTO {

    @Getter
    public static class UploadReviewImageDTO {
        //@Schema(description = "반드시 파일명.확장자 형식 리스트로 줄 것", example = "image.png")
        private List<String> fileNames;
        private Long facilityId;
    }

    @Getter
    public static class UploadOtherImageDTO {
        //@Schema(description = "반드시 파일명.확장자 형식 리스트로 줄 것", example = "image.png")
        private List<String> fileNames;
        private String type;
        private Long id;
    }

    @Getter
    public static class GetImagePageDTO {
        @Nullable
        private String continuationToken;
        private int size;
    }
}
