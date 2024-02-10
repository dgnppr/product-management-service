package me.dgpr.domains.manager.service;

import static me.dgpr.domains.manager.service.LogoutManager.generateLogoutKey;

import java.util.HashSet;
import java.util.Set;
import me.dgpr.domains.manager.usecase.QueryLogoutByIdUseCase;
import me.dgpr.persistence.service.RedisService;
import org.springframework.stereotype.Service;

@Service
public class QueryLogoutById implements QueryLogoutByIdUseCase {

    private final RedisService redisService;

    public QueryLogoutById(final RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public boolean query(final Query query) {
        String logoutKey = generateLogoutKey(query.managerId());

        return safelyGetLogoutTokens(logoutKey).contains(query.token());
    }

    private Set<String> safelyGetLogoutTokens(final String key) {
        Object result = redisService.get(key, Set.class).orElse(null);

        if (result instanceof Set) {
            return new HashSet<>((Set<String>) result);
        }

        return new HashSet<>();
    }
}
