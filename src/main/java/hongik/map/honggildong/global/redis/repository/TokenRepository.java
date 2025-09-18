package hongik.map.honggildong.global.redis.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class TokenRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    @Value("${jwt.refresh.expiration}")
    private Long JWT_REFRESH_EXP;


    public void saveRefresh(String email, String refreshToken) {
        redisTemplate.opsForValue().set(
                "refresh:" + email,
                refreshToken, JWT_REFRESH_EXP,
                TimeUnit.MILLISECONDS
        );
    }
}

