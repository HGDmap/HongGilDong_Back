package hongik.map.honggildong.domain.direction.service;

import hongik.map.honggildong.domain.direction.dto.DirectionResponseDTO;

public interface DirectionService {

    DirectionResponseDTO getDirection(Long from, Long to);
}
