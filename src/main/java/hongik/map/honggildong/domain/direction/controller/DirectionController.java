package hongik.map.honggildong.domain.direction.controller;

import hongik.map.honggildong.domain.direction.dto.DirectionResponseDTO;
import hongik.map.honggildong.domain.direction.service.DirectionService;
import hongik.map.honggildong.domain.direction.service.pathfinding.GraphLoader;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directions")
public class DirectionController {
    private final DirectionService directionService;
    private final GraphLoader graphLoader;

    @GetMapping
    public ApiResponse<DirectionResponseDTO> getDirection(@RequestParam Long from,
                                                          @RequestParam Long to) {
        DirectionResponseDTO body = directionService.getDirection(from, to);
        return ApiResponse.onSuccess(body);

    }

    @GetMapping("/reload")
    public ApiResponse<?> load() {
        graphLoader.load();
        return ApiResponse.onSuccess("노드 및 엣지 정보가 메모리에 성공적으로 업데이트되었습니다.");

    }
}
