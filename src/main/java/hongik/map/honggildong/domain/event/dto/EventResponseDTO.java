package hongik.map.honggildong.domain.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
        String name;
        String location;
        Boolean isEventOpen;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd")
        LocalDateTime eventStart;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd")
        LocalDateTime eventEnd;
        String image;
    }
}
