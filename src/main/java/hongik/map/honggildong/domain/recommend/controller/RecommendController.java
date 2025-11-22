package hongik.map.honggildong.domain.recommend.controller;

import hongik.map.honggildong.domain.recommend.dto.RecommendResponseDTO;
import hongik.map.honggildong.domain.recommend.service.RecommendService;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommend/places")
@Tag(name = "추천 시설")
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping
    public ApiResponse<RecommendResponseDTO.General> getRecommend() {
        RecommendResponseDTO.General body = recommendService.getRecommendation();
        return ApiResponse.onSuccess(body);
    }
}
