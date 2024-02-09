package me.dgpr.domains.manager.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PasswordEncoderTest {

    private static final Logger log = LoggerFactory.getLogger(PasswordEncoderTest.class);
    private final PasswordEncoder sut = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    void 비밀번호를_인코딩한다() {
        //Arrange
        var originalPassword = "password";

        //Act
        String actual = sut.encode(originalPassword);

        //Assert
        log.info("encoded password: {}", actual);
        assertThat(actual).isNotEqualTo(originalPassword);
    }
}