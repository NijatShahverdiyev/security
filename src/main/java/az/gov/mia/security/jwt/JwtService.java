package az.gov.mia.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JwtService {

    private String secret= "sdjfbsdflbsfkjbdkjbdvievbkejdbsjcsdbvkjsdvkhsvdbksdjbsdjfsdbfkjsdfbksjdfbds";
    private Key key;

    public JwtService() {
        byte[] keyBytes;
        keyBytes = secret.getBytes();
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String issueToken() {
        Duration duration = Duration.ofSeconds(3600);
        final JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject("Nijat")
                .setIssuedAt(new Date())
                .claim("roles", List.of("ROLE_ADMIN", "ROLE_USER"))
                .claim("pin", "5TG2DL7")
                .setExpiration(Date.from(Instant.now().plus(duration)))
                .signWith(key, SignatureAlgorithm.HS512);
        return jwtBuilder.compact();
    }

    public Claims parseToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }
}
