package hongik.map.honggildong.global.redis.config;

import com.redis.lettucemod.RedisModulesClient;
import com.redis.lettucemod.api.StatefulRedisModulesConnection;
import com.redis.lettucemod.api.sync.RedisModulesCommands;
import io.lettuce.core.RedisURI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        return template;
    }

    /**
     * 여기서부터 Redis Stack 관련 Bean
     */

    @Bean(destroyMethod = "close")
    public RedisModulesClient redisModulesClient() {
        RedisURI uri = RedisURI.Builder.redis(host, port).build();
        return RedisModulesClient.create(uri);
    }

    @Bean(destroyMethod = "close")
    public StatefulRedisModulesConnection<String, String> redisConnection(RedisModulesClient client) {
        return client.connect();
    }

    @Bean
    public RedisModulesCommands<String, String> redisCommands(StatefulRedisModulesConnection<String, String> connection) {
        return connection.sync();
    }
}

