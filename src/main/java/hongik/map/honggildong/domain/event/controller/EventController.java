package hongik.map.honggildong.domain.event.controller;

import hongik.map.honggildong.domain.event.dto.EventResponseDTO;
import hongik.map.honggildong.domain.event.service.EventService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
