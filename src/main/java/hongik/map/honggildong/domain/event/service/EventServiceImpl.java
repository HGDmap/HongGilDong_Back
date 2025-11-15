package hongik.map.honggildong.domain.event.service;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.event.dto.EventResponseDTO;
import hongik.map.honggildong.domain.event.entity.Event;
import hongik.map.honggildong.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public EventResponseDTO.All getAllEvents() {
        List<Event> eventList = eventRepository.findAll().stream().toList();

        List<EventResponseDTO.Single> events = eventList.stream().map(
                event -> {
                    return EventResponseDTO.Single.builder()
                            .name(event.getName())
                            .location(event.getNode().getName())
                            .image(event.getMainImg())
                            .eventEnd(event.getEndTime())
                            .eventStart(event.getStartTime())
                            .build();
                }).toList();

        return new EventResponseDTO.All(events);
    }
}
