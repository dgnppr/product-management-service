package me.dgpr.config.security;

import static me.dgpr.config.security.JwtTokenHandler.SUBJECT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;

class JwtTokenHandlerTest {

    public static final String SECRET_KEY = "zOlJAgjm9iEZPqmzilEMh4NxvOfg1qBRP3xYkzUWpSE";
    private JwtTokenHandler sut = new JwtTokenHandler();

    @Test
    void id와_비밀키와_발급시간과_만료시간을_사용하여_토큰을_생성한다() {
        //Arrange
        var id = 1L;
        var issuedAt = new Date();
        var expirationTime = 2000L;

        //Act
        String actual = sut.generateToken(
                id,
                SECRET_KEY,
                issuedAt,
                expirationTime
        );

        //Assert
        SecretKey key = sut.getSignKey(SECRET_KEY);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(actual)
                .getBody();

        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(SUBJECT);
        assertThat(claims.getIssuedAt()).isCloseTo(issuedAt, 2000); // 발급 시간 검증 (오차 범위 1000ms)
        assertThat(claims.getExpiration()).isAfter(issuedAt); // 만료 시간이 발급 시간 이후인지 검증
    }

    @Test
    void 비밀키를_사용하여_유효한_토큰을_검증한다() {
        //Arrange
        var id = 1L;
        var issuedAt = new Date();
        var expirationTime = 2000L;

        var token = sut.generateToken(
                id,
                SECRET_KEY,
                issuedAt,
                expirationTime
        );

        //Act & Assert
        assertThatNoException().isThrownBy(() -> {
            sut.verifyToken(
                    SECRET_KEY,
                    token
            );
        });
    }

    @Test
    void 비밀키를_사용하여_토큰에서_id를_추출한다() {
        //Arrange
        var id = 1L;
        var issuedAt = new Date();
        var expirationTime = 2000L;

        var token = sut.generateToken(
                id,
                SECRET_KEY,
                issuedAt,
                expirationTime
        );

        //Act
        Long actual = sut.getIdFromToken(
                SECRET_KEY,
                token
        );

        //Assert
        assertThat(actual).isEqualTo(id);
    }
}