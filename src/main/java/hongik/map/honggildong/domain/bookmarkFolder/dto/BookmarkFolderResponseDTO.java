package hongik.map.honggildong.domain.bookmarkFolder.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import hongik.map.honggildong.domain.facility.entity.Facility;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class BookmarkFolderResponseDTO {

    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class Single {
        private Long folderId;
        private String folderName;
        private String color;
        private List<BookmarkList> bookmarkList; // 폴더 내 북마크들

        @JsonProperty("bookmarkCount")
        public long getBookmarkCount() {
            return (bookmarkList == null) ? 0L : bookmarkList.size();
        }
    }

    @Builder @Getter @NoArgsConstructor @AllArgsConstructor
    public static class BookmarkList {
        private Long facilityId;
        private Double latitude;
        private Double longitude;
    }
}
