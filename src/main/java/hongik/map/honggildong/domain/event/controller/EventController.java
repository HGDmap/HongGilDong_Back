package hongik.map.honggildong.domain.event.controller;

import hongik.map.honggildong.domain.event.dto.EventResponseDTO;
import hongik.map.honggildong.domain.event.service.EventService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "이벤트")
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    // 전체 이벤트 조회
    @GetMapping
    public ApiResponse<EventResponseDTO.All> getAllEvents() {

        EventResponseDTO.All body = eventService.getAllEvents();
        return ApiResponse.onSuccess(body);
    }

    // 이벤트 상세 정보 조회
    @GetMapping("/{eventId}")
    public ApiResponse<EventResponseDTO.Detail> getEventDetail (@PathVariable("eventId") Long eventId) {

        EventResponseDTO.Detail body = eventService.getEventDetail(eventId);
        return ApiResponse.onSuccess(body);
    }

}
