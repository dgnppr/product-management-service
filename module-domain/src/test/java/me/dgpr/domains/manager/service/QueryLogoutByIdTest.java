package me.dgpr.domains.manager.service;

import static me.dgpr.domains.manager.service.LogoutManager.KEY_PREFIX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import me.dgpr.domains.manager.usecase.QueryLogoutByTokenUseCase;
import me.dgpr.persistence.service.RedisService;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QueryLogoutByIdTest {

    @Mock
    private RedisService redisService;

    @InjectMocks
    private QueryLogoutByToken sut;

    @Test
    void 로그아웃된_토큰이면_true를_반환한다() {
        //Arrange
        var managerId = 1L;
        var token = "token";
        var redisKey = KEY_PREFIX + 1L;
        var query = new QueryLogoutByTokenUseCase.Query(
                managerId,
                token
        );

        when(redisService.get(
                eq(redisKey),
                any())
        ).thenReturn(Optional.of(Set.of(token)));

        //Act
        boolean actual = sut.query(query);

        //Assert
        assertThat(actual).isTrue();
    }

    @Test
    void 로그아웃된_토큰이_아니면_false를_반환한다() {
        //Arrange
        var managerId = 1L;
        var token = "token";
        var redisKey = KEY_PREFIX + 1L;
        var query = new QueryLogoutByTokenUseCase.Query(
                managerId,
                token
        );

        when(redisService.get(
                eq(redisKey),
                any())
        ).thenReturn(Optional.of(Set.of("token2", "token3")));

        //Act
        boolean actual = sut.query(query);

        //Assert
        assertThat(actual).isFalse();
    }

}