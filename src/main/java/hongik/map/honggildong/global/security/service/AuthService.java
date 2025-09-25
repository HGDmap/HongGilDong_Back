package hongik.map.honggildong.global.security.service;

import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.global.security.dto.AuthRequestDTO;
import hongik.map.honggildong.global.security.dto.AuthResponseDTO;

public interface AuthService {

    AuthResponseDTO.Login signUp(AuthRequestDTO.SignUp request);
}
