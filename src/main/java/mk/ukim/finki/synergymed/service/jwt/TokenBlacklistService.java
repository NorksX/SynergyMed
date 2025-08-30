package mk.ukim.finki.synergymed.service.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final StringRedisTemplate redis;

    public void add(String jti, long expirationMillis) {
        redis.opsForValue().set(
                "blacklist:" + jti,
                "revoked",
                Duration.ofMillis(expirationMillis)
        );
    }

    public boolean contains(String jti) {
        return redis.hasKey("blacklist:" + jti);
    }
}
