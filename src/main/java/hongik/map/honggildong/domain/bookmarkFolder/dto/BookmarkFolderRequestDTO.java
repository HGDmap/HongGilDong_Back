package hongik.map.honggildong.domain.bookmarkFolder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BookmarkFolderRequestDTO {

    @Builder @Getter @NoArgsConstructor @AllArgsConstructor
    public static class Create {
        @NotBlank
        private String folderName;
        @NotBlank
        private String folderColor;
    }

    @Builder @Getter @NoArgsConstructor @AllArgsConstructor
    public static class Update {
        private String folderName;
        private String folderColor;
    }
}
