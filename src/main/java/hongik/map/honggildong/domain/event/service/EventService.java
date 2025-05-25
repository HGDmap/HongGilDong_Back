package hongik.map.honggildong.domain.event.service;

import hongik.map.honggildong.domain.event.dto.EventResponseDTO;
import hongik.map.honggildong.domain.event.entity.Event;

import java.util.List;

public interface EventService {

    List<Event> getEventListOf();

    EventResponseDTO.PinEvent getEventListOf(double latitude, double longitude);

    Event getEvent(long eventId);
}
