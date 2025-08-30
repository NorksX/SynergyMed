package mk.ukim.finki.synergymed.web;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import mk.ukim.finki.synergymed.service.jwt.JwtService;
import mk.ukim.finki.synergymed.service.jwt.TokenBlacklistService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    public LogoutController(JwtService jwtService, TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping()
    public String logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            Jws<Claims> jws = jwtService.parse(token);
            Claims claims = jws.getPayload();

            long expirationMillis = claims.getExpiration().getTime() - System.currentTimeMillis();
            if (expirationMillis > 0) {
                tokenBlacklistService.add(claims.getId(), expirationMillis);
            }
        }
        return "redirect:/login";
    }
}
