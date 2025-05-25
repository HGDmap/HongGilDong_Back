package hongik.map.honggildong.domain.event.dto;

import hongik.map.honggildong.domain.event.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDTO {

    public static class EventSummary {
        private Long eventId;
        private String eventName;
        private String eventLocation;
        private boolean isEventOpen;
        private Date eventStart;
        private Date eventEnd;
        private String eventImage;
    }

    public static class General extends EventSummary{
        BuildingSummary buildingData;
    }

    //BuildingSummary 데이터

    public static class All{
        private Long buildingId;
        private Double latitude;
        private Double longitude;
    }

    public static class PinEvent{
        private String buildingName;
        private List<EventSummary> eventist;
    }


}
