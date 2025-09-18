package hongik.map.honggildong.global.security.exception;

import hongik.map.honggildong.global.apiPayload.code.BaseCode;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;

public class TokenException extends GeneralException {

    public TokenException(BaseCode code) {
        super(code);
    }
}
