package daniil.dobris.authorizationbot.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    private SecretKey key;
    private final long expirationMs = 1000 * 60 * 60;
    private final String secret = "f6de82ede2a0197534deea7ac953f91b24b56e95c75c2680bf0fe99de94513a3";
    private final Algorithm algorithm = Algorithm.HMAC256(secret);

    public String generateToken(Long userId) {
        return JWT.create()
                .withSubject(userId.toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMs))
                .sign(algorithm);
    }

    public Long validateTokenAndGetUserId(String token) {
        try {
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            return Long.parseLong(jwt.getSubject());
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}
