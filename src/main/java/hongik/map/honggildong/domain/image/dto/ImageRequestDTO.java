package hongik.map.honggildong.domain.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.List;

public class ImageRequestDTO {

    @Getter
    public static class UploadImageDTO {
        //@Schema(description = "반드시 파일명.확장자 형식 리스트로 줄 것", example = "image.png")
        private List<String> fileNames;
        private Long facilityId;
    }

    @Getter
    public static class GetImagePageDTO {
        @Nullable
        private String continuationToken;
        private int size;
    }
}
