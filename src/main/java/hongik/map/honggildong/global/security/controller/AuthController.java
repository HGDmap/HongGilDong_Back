package hongik.map.honggildong.global.security.controller;

import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.security.dto.AuthRequestDTO;
import hongik.map.honggildong.global.security.dto.AuthResponseDTO;
import hongik.map.honggildong.global.security.jwt.JwtTokenProvider;
import hongik.map.honggildong.global.security.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signin")
    @Operation(summary = "로그인", description = "로그인 api, 실제 동작은 시큐리티 필터로 수행합니다.")
    public ApiResponse<AuthResponseDTO.Login> signIn(@Valid @RequestBody AuthRequestDTO.Login request) {
        return null;
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "이메일 인증 완료 후 최종 회원 가입에 대한 api")
    public ApiResponse<AuthResponseDTO.Login> signUp(@Valid @RequestBody AuthRequestDTO.SignUp request){

        AuthResponseDTO.Login body = authService.signUp(request);

        return ApiResponse.onSuccess(body);
    }

    @GetMapping("/random")
    @Operation(summary = "임시 api", description = "토큰을 이용한 요청 검증을 위한 임시 API")
    public ApiResponse<String> random(){
        return ApiResponse.onSuccess("성공입니다");
    }

}
