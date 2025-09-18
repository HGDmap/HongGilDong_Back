package hongik.map.honggildong.global.security.exception;

import hongik.map.honggildong.global.apiPayload.code.BaseCode;
import hongik.map.honggildong.global.apiPayload.code.ReasonDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum SecurityExceptionCode implements BaseCode {
    INVALID_USERNAME_OR_PASSWORD(HttpStatus.NOT_FOUND, "SECURITY400","아이디 또는 비밀번호가 틀립니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "SECURITY401","유효하지 않은 토큰입니다."),
    REFRESH_EXPIRED(HttpStatus.UNAUTHORIZED, "SECURITY402","로그인이 만료되었습니다. 다시 로그인해 주세요."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "SECURITY403","유효하지 않은 서명입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "SECURITY404","만료된 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
