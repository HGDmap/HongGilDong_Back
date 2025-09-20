package hongik.map.honggildong.global.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuthResponseDTO{

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Login{
        @NotBlank
        private String accessToken;
        @NotBlank
        private String refreshToken;
    }


}
