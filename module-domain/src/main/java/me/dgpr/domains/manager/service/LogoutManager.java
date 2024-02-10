package me.dgpr.domains.manager.service;

import java.util.HashSet;
import java.util.Set;
import me.dgpr.domains.manager.usecase.LogoutManagerUseCase;
import me.dgpr.persistence.service.RedisService;
import org.springframework.stereotype.Service;

@Service
public class LogoutManager implements LogoutManagerUseCase {

    public static final String KEY_PREFIX = "logout:";
    private final RedisService logoutRedisService;

    public LogoutManager(final RedisService logoutRedisService) {
        this.logoutRedisService = logoutRedisService;
    }

    @Override
    public void command(final Command command) {
        String key = generateLogoutKey(command.managerId());

        Set<String> logoutTokens = safelyGetLogoutTokens(key);

        logoutTokens.add(command.token());

        logoutRedisService.set(
                key,
                logoutTokens,
                command.duration()
        );
    }

    private Set<String> safelyGetLogoutTokens(final String key) {
        Object result = logoutRedisService.get(key, Set.class).orElse(null);

        if (result instanceof Set) {
            return new HashSet<>((Set<String>) result);
        }

        return new HashSet<>();
    }

    public static String generateLogoutKey(final Long id) {
        return KEY_PREFIX + id;
    }
}
