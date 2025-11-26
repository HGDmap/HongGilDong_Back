package hongik.map.honggildong.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class All {
        List<Single> events;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Single {
        Long id;
        String name;
        String location;
        Double latitude;
        Double longitude;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd")
        LocalDate eventStart;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd")
        LocalDate eventEnd;
        String image;

        @JsonProperty("isEventOpen")
        public Boolean getEventOpen() {
            LocalDate todayKST = LocalDate.now(ZoneId.of("Asia/Seoul"));
            return !todayKST.isBefore(eventStart) && !todayKST.isAfter(eventEnd);
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {
        Long id;
        String name;
        String location;
        String image;
        Info eventInfo;
        Location locationInfo;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd")
        LocalDate eventStart;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy.MM.dd")
        LocalDate eventEnd;

        String callNumber;
        String homepage;

        @JsonProperty("isEventOpen")
        public Boolean getEventOpen() {
            LocalDate todayKST = LocalDate.now(ZoneId.of("Asia/Seoul"));
            return !todayKST.isBefore(eventStart) && !todayKST.isAfter(eventEnd);
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {
        String buildingName;
        Long nodeId;
        List<String> images = new ArrayList<>();
    }
}
