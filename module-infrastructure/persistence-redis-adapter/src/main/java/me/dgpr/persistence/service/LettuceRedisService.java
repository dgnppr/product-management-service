package me.dgpr.persistence.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LettuceRedisService implements RedisService {

    private static final Logger log = LoggerFactory.getLogger(LettuceRedisService.class);
    private static final String KEY_PREFIX = "me.dgpr.product-management:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public LettuceRedisService(
            final StringRedisTemplate redisTemplate,
            final ObjectMapper objectMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> Optional<T> get(
            final String key,
            final Class<T> type
    ) {
        String serializedValue = redisTemplate.opsForValue().get(redisKey(key));
        try {
            return Optional.of(objectMapper.readValue(serializedValue, type));
        } catch (Exception e) {
            log.error("Error retrieving key {} from Redis", redisKey(key), e);
        }
        return Optional.empty();
    }

    @Override
    public void set(
            final String key,
            final Object value,
            final Duration duration
    ) {
        try {
            String serializedValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(redisKey(key), serializedValue, duration);
        } catch (Exception e) {
            log.error("Error saving key {} to Redis", redisKey(key), e);
        }
    }

    @Override
    public boolean setIfAbsent(
            final String key,
            final Object value,
            final Duration duration
    ) {
        try {
            String serializedValue = objectMapper.writeValueAsString(value);
            return Boolean.TRUE.equals(redisTemplate.opsForValue()
                    .setIfAbsent(redisKey(key), serializedValue, duration));
        } catch (Exception e) {
            log.error("Error in setIfAbsent for key {} in Redis", redisKey(key), e);
        }
        return false;
    }

    @Override
    public boolean delete(final String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(redisKey(key)));
    }

    private String redisKey(final String key) {
        return KEY_PREFIX + key;
    }
}
