package com.example.socialnetwork.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author RKomov
 */
@Configuration
public class MessagingConfig {


    public static final String HISTORY_QUEUE_NAMES = "history";
    public static final String HISTORY_EXCHANGE_NAME = "history";

    @Bean
    Queue queue() {
        return new Queue(HISTORY_QUEUE_NAMES, true);
    }

    @Bean
    Jackson2JsonMessageConverter converter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return new Jackson2JsonMessageConverter(objectMapper);
    }

//    @Bean
//    DirectExchange exchange() {
//        return new DirectExchange(EXCHANGE_NAME);
//    }
//
//    @Bean
//    Binding binding(Queue queue, DirectExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("*");
//    }

}
