package hongik.map.honggildong.domain.bookmark.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import hongik.map.honggildong.domain.bookmarkFolder.dto.BookmarkFolderResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class BookmarkResponseDTO {

    // 단일 즐겨찾기 폴더 & 즐겨찾기 정보
    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class Single {
        private Long folderId;
        private String folderName;
        private String color;
        private List<BookmarkResponseDTO.Detail> bookmarkList; // 폴더 내 북마크들

        @JsonProperty("bookmarkCount")
        public long getBookmarkCount() {
            return (bookmarkList == null) ? 0L : bookmarkList.size();
        }
    }

    // 전체 즐겨찾기 폴더 & 즐겨찾기 정보
    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class All {
        private List<BookmarkResponseDTO.Single> bookmarkFolderList;
    }

    // 시설 세부 정보
    @Builder @Getter @NoArgsConstructor @AllArgsConstructor
    public static class Detail {
        private Long facilityId;
        private String facilityName;
        private String facilityLocation;

        @JsonIgnore
        private String openInfo;

        private String facilityImage;
        private Double latitude;
        private Double longitude;

        private Long buildingId;

        //TODO: 시설 open info 처리
//        @JsonProperty("isFacilityOpen")
//        public Boolean isFacilityOpen() {
//            return OpeningHoursParser.isOpenNow(openInfo, ZoneId.of("Asia/Seoul"));
//        }
//
//        // open info로 영업 상태 반환
//        static class OpeningHoursParser {
//            private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("HH:mm");
//            private static final Map<String, DayOfWeek> MAP = Map.of(
//                    "월", DayOfWeek.MONDAY,
//                    "화", DayOfWeek.TUESDAY,
//                    "수", DayOfWeek.WEDNESDAY,
//                    "목", DayOfWeek.THURSDAY,
//                    "금", DayOfWeek.FRIDAY,
//                    "토", DayOfWeek.SATURDAY,
//                    "일", DayOfWeek.SUNDAY
//            );
//
//            static Boolean isOpenNow(String text, ZoneId zone) {
//                if (text == null || text.isBlank()) return null;
//                DayOfWeek today = ZonedDateTime.now(zone).getDayOfWeek();
//                LocalTime now = LocalTime.now(zone);
//
//                for (String line : text.split("[/\\n]+")) {
//                    line = line.trim();
//                    if (line.isEmpty()) continue;
//
//                    // 요일 시간 추출
//                    String[] parts = line.split("\\s+");
//                    if (parts.length < 2) continue;
//
//                    String dayPart = parts[0];
//                    String timePart = parts[1];
//                    if (!MAP.containsKey(dayPart)) continue;
//
//                    if (MAP.get(dayPart) != today) continue;
//
//                    String[] range = timePart.split("-");
//                    if (range.length != 2) continue;
//
//                    LocalTime start = LocalTime.parse(range[0], TF);
//                    LocalTime end = LocalTime.parse(range[1], TF);
//
//                    // 자정 넘어가는 경우
//                    if (end.isBefore(start)) {
//                        return !now.isBefore(start) || !now.isAfter(end);
//                    } else {
//                        return !now.isBefore(start) && !now.isAfter(end);
//                    }
//                }
//                return false;
//            }
//        }
    }
}
