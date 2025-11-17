package hongik.map.honggildong.domain.event.service;

import hongik.map.honggildong.domain.bookmark.dto.BookmarkResponseDTO;
import hongik.map.honggildong.domain.building.entity.Building;
import hongik.map.honggildong.domain.building.repository.BuildingRepository;
import hongik.map.honggildong.domain.event.dto.EventResponseDTO;
import hongik.map.honggildong.domain.event.entity.Event;
import hongik.map.honggildong.domain.event.repository.EventRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final BuildingRepository buildingRepository;

    @Override
    public EventResponseDTO.All getAllEvents() {
        List<Event> eventList = eventRepository.findAll().stream().toList();

        List<EventResponseDTO.Single> events = eventList.stream().map(
                event -> {
                    return EventResponseDTO.Single.builder()
                            .id(event.getId())
                            .name(event.getName())
                            .location(event.getDescription())
                            .longitude(event.getNode().getLongitude())
                            .latitude(event.getNode().getLatitude())
                            .image(event.getMainImg())
                            .eventEnd(event.getEndTime())
                            .eventStart(event.getStartTime())
                            .build();
                }).toList();

        return new EventResponseDTO.All(events);
    }

    @Override
    public EventResponseDTO.Detail getEventDetail(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.EVENT_NOT_FOUND));

        EventResponseDTO.Info info = EventResponseDTO.Info.builder()
                .callNumber(event.getCallNumber())
                .homepage(event.getHomepage())
                .eventStart(event.getStartTime())
                .eventEnd(event.getEndTime())
                .build();

        EventResponseDTO.Location location = EventResponseDTO.Location.builder()
                .images(Collections.singletonList(event.getNode().getBuilding().getMainImg()))
                .nodeId(event.getNode().getId())
                .buildingName(event.getNode().getBuilding().getName())
                .build();

        return new EventResponseDTO.Detail(
                eventId,
                event.getName(),
                event.getDescription(),
                event.getMainImg(),
                info,
                location);
    }
}
