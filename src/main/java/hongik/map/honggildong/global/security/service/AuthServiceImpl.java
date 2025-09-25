package hongik.map.honggildong.global.security.service;


import hongik.map.honggildong.domain.member.converter.MemberConverter;
import hongik.map.honggildong.domain.member.entity.Member;
import hongik.map.honggildong.domain.member.repository.MemberRepository;
import hongik.map.honggildong.global.apiPayload.code.status.ErrorStatus;
import hongik.map.honggildong.global.apiPayload.exception.GeneralException;
import hongik.map.honggildong.global.security.dto.AuthRequestDTO;
import hongik.map.honggildong.global.security.dto.AuthResponseDTO;
import hongik.map.honggildong.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final MemberRepository memberRepository;

    @Override
    public AuthResponseDTO.Login signUp(AuthRequestDTO.SignUp request) {

        if(memberRepository.existsByEmail(request.getEmail())){
            throw new GeneralException(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }

        String encodedPW = passwordEncoder.encode(request.getPassword());
        Member member = MemberConverter.toMember(request.getFullName(),request.getNickname(),request.getEmail(), encodedPW);

        memberRepository.save(member);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getEmail(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.generateAccessToken(authentication, member.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication, member.getEmail());

        //redis에 refresh 등록 과정 필요

        return AuthResponseDTO.Login.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }
}
