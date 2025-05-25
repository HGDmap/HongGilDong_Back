package hongik.map.honggildong.domain.event.controller;

import hongik.map.honggildong.domain.event.dto.EventResponseDTO;
import hongik.map.honggildong.domain.event.entity.Event;
import hongik.map.honggildong.domain.event.service.EventService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
@Tag(name = "이벤트")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ApiResponse<EventResponseDTO.All> getAllEvent(){
        List<Event> events = eventService.getEventListOf();
        return ApiResponse.onSuccess(events);
    }

    @GetMapping("/pin")
    public ApiResponse<EventResponseDTO.PinEvent> getPinEvent(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude) {
        EventResponseDTO.PinEvent events = eventService.getEventListOf(latitude, longitude);
        return ApiResponse.onSuccess(events);
    }

    @GetMapping("/{eventId}")
    public ApiResponse<EventResponseDTO.General> getEventDetail(@PathVariable long eventId){
        Event event = eventService.getEvent(eventId);
        return ApiResponse.onSuccess(event);
    }
}
