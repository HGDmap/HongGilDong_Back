package hongik.map.honggildong.domain.recommend.service;

import hongik.map.honggildong.domain.recommend.dto.RecommendResponseDTO;
import hongik.map.honggildong.global.security.service.CustomUserDetails;

public interface RecommendService {

    RecommendResponseDTO.General getRecommendation(CustomUserDetails user);
}
