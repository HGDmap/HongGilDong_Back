package hongik.map.honggildong.global.security.controller;

import hongik.map.honggildong.global.security.dto.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
public class AuthController {

    @PostMapping("/signIn")
    @Operation(summary = "로그인", description = "로그인 api, 실제 동작은 시큐리티 필터로 수행합니다.")
    public void signIn(@RequestBody LoginRequest loginRequest) {

    }

}
