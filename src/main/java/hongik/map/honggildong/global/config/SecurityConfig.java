package hongik.map.honggildong.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

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
                );

        //세션 설정: 무상태로
        http.sessionManagement((session)->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }

}
