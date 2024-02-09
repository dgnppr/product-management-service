package me.dgpr.config.security;

import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenHandler {

    public static final String ID = "id";
    public static final String SUBJECT = "authentication";

    public void verifyToken(
            String secretKey,
            String token
    ) {
        try {
            Claims claims = doVerifyTokenAndGetClaims(secretKey, token);
            if (!Objects.equals(claims.getSubject(), SUBJECT)) {
                throw new JwtException("Invalid subject in the token");
            }
        } catch (Exception e) {
            throw new JwtException(e.getMessage(), e);
        }
    }

    public String generateToken(
            Long id,
            String secretKey,
            Date issuedAt,
            long expiredTimeMs
    ) {
        return Jwts.builder()
                .signWith(getSignKey(secretKey))
                .claim(ID, id)
                .setSubject(SUBJECT)
                .setIssuedAt(issuedAt)
                .setExpiration(new Date(issuedAt.getTime() + expiredTimeMs))
                .compact();
    }

    public Long getIdFromToken(
            String secretKey,
            String token
    ) {
        try {
            Claims claims = doVerifyTokenAndGetClaims(secretKey, token);
            return claims.get(ID, Long.class);
        } catch (Exception e) {
            throw new JwtException(e.getMessage(), e);
        }
    }

    private Claims doVerifyTokenAndGetClaims(
            String secretKey,
            String token
    ) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public SecretKey getSignKey(String secretKey) {
        return hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
