package com.example.socialnetwork.config;

import com.example.socialnetwork.model.HistoryItem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author RKomov
 */
@Configuration
public class HistoryCacheConfig {

    @Bean
    public RedisTemplate<String, HistoryItem>  redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, HistoryItem> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // explicitly enable transaction support
        template.setEnableTransactionSupport(true);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        return template;
    }
}
