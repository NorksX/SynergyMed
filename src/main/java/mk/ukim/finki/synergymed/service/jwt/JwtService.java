package mk.ukim.finki.synergymed.service.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtService {
    private final SecretKey key;     // <— SecretKey
    private final long expMillis;

    public JwtService(
            @Value("${security.jwt.secret:${SECURITY_JWT_SECRET:}}") String secret,
            @Value("${security.jwt.exp-min:${SECURITY_JWT_EXP_MIN:60}}") long expMin
    ) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("security.jwt.secret (or env SECURITY_JWT_SECRET) must be ≥ 32 chars");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expMillis = expMin * 60_000;
    }

    public String generate(UserDetails ud) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expMillis);
        List<String> roles = ud.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(ud.getUsername())     // 0.12.x
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)                 // <— let JJWT infer HS256/384/512 from key size
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()               // 0.12.x
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }
}
