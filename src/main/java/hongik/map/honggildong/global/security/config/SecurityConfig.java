package hongik.map.honggildong.global.security.config;

import hongik.map.honggildong.global.redis.repository.TokenRepository;
import hongik.map.honggildong.global.security.filter.JwtAuthenticationFilter;
import hongik.map.honggildong.global.security.filter.JwtAuthorizationFilter;
import hongik.map.honggildong.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenProvider, tokenRepository);
        jwtAuthenticationFilter.setFilterProcessesUrl("/auth/signIn");

        http
                .csrf(auth->auth.disable())
                .httpBasic(auth->auth.disable())
                .formLogin(auth->auth.disable());

        //경로 권한 설정
        http
                .authorizeHttpRequests(auth->auth
                        //스웨거 docs 관련 모든 경로 허용
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(
                                "/auth/**"
                        ).permitAll()
                        .requestMatchers("/auth/random").authenticated()
                        .anyRequest().authenticated()
                );

        //세션 설정: 무상태로
        http.sessionManagement((session)->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthorizationFilter, JwtAuthenticationFilter.class);


        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        //BCrypt 인코더 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
