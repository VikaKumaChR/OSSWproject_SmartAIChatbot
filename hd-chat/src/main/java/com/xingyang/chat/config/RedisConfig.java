package com.xingyang.chat.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import java.time.Duration;

/**
 * Redis Configuration
 *
 * @author XingYang
 */
@Slf4j
@Configuration
@EnableCaching
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void init() {
        try {
            // 测试Redis连接
            String testKey = "test:connection";
            String testValue = "Connected at " + System.currentTimeMillis();
            
            stringRedisTemplate.opsForValue().set(testKey, testValue);
            String retrievedValue = stringRedisTemplate.opsForValue().get(testKey);
            
            if (retrievedValue != null && retrievedValue.equals(testValue)) {
                log.info("Redis connection test successful");
            } else {
                log.warn("Redis connection test failed - value mismatch");
            }
            
            // 清理测试键
            stringRedisTemplate.delete(testKey);
            
        } catch (Exception e) {
            log.error("Redis connection test failed: {}", e.getMessage(), e);
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        log.info("Initializing Redis template");
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        
        // Use Jackson2JsonRedisSerializer for value serialization
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getJackson2JsonRedisSerializer();
        
        // Use StringRedisSerializer for key serialization
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        
        // Key serialization
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        
        // Value serialization
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        
        template.afterPropertiesSet();
        log.info("Redis template initialized successfully");
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        log.info("Initializing Redis cache manager");
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getJackson2JsonRedisSerializer();
        
        // Configure cache serialization
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1)) // TTL for cache entries
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();
        
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
        
        log.info("Redis cache manager initialized successfully");
        return cacheManager;
    }
    
    private Jackson2JsonRedisSerializer<Object> getJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, 
                ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        
        return serializer;
    }
} 