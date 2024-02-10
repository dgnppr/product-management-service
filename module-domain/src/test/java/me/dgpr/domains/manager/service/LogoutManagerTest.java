package me.dgpr.domains.manager.service;

import static me.dgpr.domains.manager.service.LogoutManager.KEY_PREFIX;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import me.dgpr.domains.manager.usecase.LogoutManagerUseCase;
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
class LogoutManagerTest {

    @Mock
    private RedisService logoutRedisService;

    @InjectMocks
    private LogoutManager sut;

    @Test
    void 로그아웃된_토큰을_redis에_저장한다() {
        //Arrange
        var managerId = 1L;
        var loggedOutToken = "token3";
        var ttl = Duration.ofMillis(2000L);
        var redisKey = KEY_PREFIX + managerId;

        var command = new LogoutManagerUseCase.Command(
                managerId,
                loggedOutToken,
                ttl
        );

        when(logoutRedisService.get(
                eq(redisKey),
                any()
        )).thenReturn(Optional.of(Set.of("token1", "token2")));

        //Act
        sut.command(command);

        //Assert
        verify(logoutRedisService)
                .set(redisKey, Set.of("token1", "token2", "token3"), ttl);
    }
}