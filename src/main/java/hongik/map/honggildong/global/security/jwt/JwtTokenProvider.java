package hongik.map.honggildong.global.security.jwt;

import hongik.map.honggildong.global.security.exception.TokenException;
import hongik.map.honggildong.global.security.service.CustomUserDetails;
import hongik.map.honggildong.global.security.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static hongik.map.honggildong.global.security.exception.SecurityExceptionCode.*;
import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.secretKey}")
    private String secret;
    @Value("${jwt.access.expiration}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;
    @Value("${jwt.refresh.expiration}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    private SecretKey key;

    @PostConstruct
    protected void initSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Authentication authentication, String email) {
        return generateToken(authentication, ACCESS_TOKEN_EXPIRE_TIME, "access", email);
    }

    public String generateRefreshToken(Authentication authentication, String email) {
        return generateToken(authentication, REFRESH_TOKEN_EXPIRE_TIME, "refresh", email);
    }

    private String generateToken(Authentication authentication, Long expirationMs, String category, String userId) {

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(userId)
                .claim("category", category)
                .claim("auth", authorities)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    public Boolean validateToken(String token){
        try {
            Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (MalformedJwtException e) {
            throw new TokenException(INVALID_TOKEN);
        } catch (SecurityException e) {
            throw new TokenException(INVALID_SIGNATURE);
        }
    }

    public String reIssueToken(String refreshToken){

        Authentication authentication = getAuthentication(refreshToken);
        String userId = getSubject(refreshToken);
        return generateAccessToken(authentication, userId);
    }

    public Authentication getAuthentication(String token){
        Claims claims = parseClaims(token);

        //새로운 accessToken에 넣을 권한 추출 과정
        List<GrantedAuthority> authorities = Stream.of(claims.get("auth", String.class).split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        CustomUserDetails principal = customUserDetailsService.loadUserByUsername(claims.get("email", String.class));
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }


}
