package hongik.map.honggildong.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkResponseDTO {

    public static class AllBookmarks {
        private List<ColorBookmarkGroup> allBookmarkList;

    }

    // 색상별 그룹 (BLUE, YELLOW, RED 등)
    public static class ColorBookmarkGroup {
        private String color;
        private List<BookmarkInfo> bookmarkList;

    }

    // 개별 즐겨찾기 정보
    public static class BookmarkInfo {
        private Long facilityId;
        private double latitude;
        private double longitude;

    }

    public static class FolderData {
        private int folderCount;
        private List<Folder> folderList;
    }

    // 각 폴더의 정보
    public static class Folder {
        private Long folderId;
        private String folderName;
        private String color;
        private int bookmarkCount;
        private List<Bookmark> bookmarkList;

    }

    // 즐겨찾기 정보
    public static class Bookmark {
        private Long facilityId;
        private double latitude;
        private double longitude;

    }

    public static class FolderDetail {
        private Long folderId;
        private String folderName;
        private String color;
        private int bookmarkCount;
        private List<FacilityBookmark> bookmarkList;


    }

    public static class FacilityBookmark {
        private Long facilityId;
        private String facilityName;
        private String facilityLocation;
        private boolean isFacilityOpen;
        private List<String> facilityImages;


    }
}
