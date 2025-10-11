package hongik.map.honggildong.domain.direction.controller;

import hongik.map.honggildong.domain.direction.dto.DirectionResponseDTO;
import hongik.map.honggildong.domain.direction.service.DirectionService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directions")
public class DirectionController {
    private final DirectionService directionService;

    @GetMapping
    public ApiResponse<DirectionResponseDTO> getDirection(@RequestParam Long from,
                                                          @RequestParam Long to) {
        DirectionResponseDTO body = directionService.getDirection(from, to);
        return ApiResponse.onSuccess(body);

    }
}
