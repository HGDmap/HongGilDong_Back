package hongik.map.honggildong.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import hongik.map.honggildong.global.apiPayload.ApiResponse;
import hongik.map.honggildong.global.redis.repository.TokenRepository;
import hongik.map.honggildong.global.security.dto.AuthRequestDTO;
import hongik.map.honggildong.global.security.dto.AuthResponseDTO;
import hongik.map.honggildong.global.security.exception.SecurityExceptionCode;
import hongik.map.honggildong.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,TokenRepository tokenRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRepository = tokenRepository;
    }

    //로그인 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        ObjectMapper om = new ObjectMapper();
        try{
            //username, pw 받기
            AuthRequestDTO.Login login  = om.readValue(request.getInputStream(), AuthRequestDTO.Login.class);

            //token 발급
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(login.getEmail(),login.getPassword());
            //로그인 여부 검증
            setDetails(request,authToken);
            //authentication 반환(AuthenticationManager 에게 인증 위임)
            return authenticationManager.authenticate(authToken);

        }catch (IOException e){
            throw new AuthenticationServiceException(e.getMessage());
        }

    }

    //로그인 성공 시 토큰 발급
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);

        String email = authResult.getName();

        String accessToken = jwtTokenProvider.generateAccessToken(authResult, email);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authResult, email);

        tokenRepository.saveRefresh(email, refreshToken);

        AuthResponseDTO.Login loginResponse = AuthResponseDTO.Login.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        ApiResponse<AuthResponseDTO.Login> body = ApiResponse.onSuccess(loginResponse);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), body);
    }

    // 인증 실패 시 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        SecurityExceptionCode loginFailure = SecurityExceptionCode.INVALID_USERNAME_OR_PASSWORD;

        ApiResponse<String> body = ApiResponse.onFailure(loginFailure.getCode(),loginFailure.getMessage(),null);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), body);
    }
}
