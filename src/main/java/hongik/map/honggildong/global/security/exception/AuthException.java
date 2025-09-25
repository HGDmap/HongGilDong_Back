package hongik.map.honggildong.global.security.exception;

import hongik.map.honggildong.global.apiPayload.code.BaseCode;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;

public class AuthException extends GeneralException {
  public AuthException(BaseCode code) {
    super(code);
  }
}
