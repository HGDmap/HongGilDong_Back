package hongik.map.honggildong.domain.event.service;

import hongik.map.honggildong.domain.event.dto.EventResponseDTO;

public interface EventService {
    EventResponseDTO.All getAllEvents();

    EventResponseDTO.Detail getEventDetail(Long eventId);
}
