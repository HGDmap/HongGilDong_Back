package hongik.map.honggildong.global.security.dto;

import lombok.Builder;

@Builder
public record LoginResponse(
        String accessToken,
        String refreshToken
){

}
