package me.dgpr.persistence.service;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogoutRedisService implements RedisService {

    private static final String KEY_PREFIX = "me.dgpr.product-management:";

    private final RedisTemplate<String, Set<String>> logoutRedisTemplate;

    public LogoutRedisService(final RedisTemplate<String, Set<String>> logoutRedisTemplate) {
        this.logoutRedisTemplate = logoutRedisTemplate;
    }

    @Override
    public <T> Optional<T> get(
            final String key,
            final Class<T> type
    ) {
        Set<String> value = logoutRedisTemplate.opsForValue().get(key);
        if (value == null) {
            return Optional.empty();
        }

        T castedValue = (T) value;
        return Optional.of(castedValue);
    }

    @Override
    public void set(
            final String key,
            final Object value,
            final Duration duration
    ) {
        if (!(value instanceof Set)) {
            throw new IllegalArgumentException("Value must be of type Set<String>");
        }

        Set<String> castedValue = (Set<String>) value;
        logoutRedisTemplate.opsForValue().set(key, castedValue, duration);
    }

    @Override
    public boolean setIfAbsent(
            final String key,
            final Object value,
            final Duration duration
    ) {
        if (!(value instanceof Set)) {
            throw new IllegalArgumentException("Value must be of type Set<String>");
        }

        Set<String> castedValue = (Set<String>) value;
        Boolean result = logoutRedisTemplate.opsForValue().setIfAbsent(key, castedValue, duration);
        return Boolean.TRUE.equals(result);
    }

    @Override
    public boolean delete(final String key) {
        Boolean result = logoutRedisTemplate.delete(key);
        return Boolean.TRUE.equals(result);
    }

    private String redisKey(final String key) {
        return KEY_PREFIX + key;
    }
}
