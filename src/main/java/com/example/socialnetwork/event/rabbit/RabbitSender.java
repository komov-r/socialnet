package com.example.socialnetwork.event.rabbit;

import com.example.socialnetwork.config.MessagingConfig;
import com.example.socialnetwork.event.HistoryEventRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author RKomov
 */
@Component
public class RabbitSender {

    Logger logger = LoggerFactory.getLogger(RabbitSender.class);

    AmqpTemplate amqpTemplate;

    public RabbitSender(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @EventListener
    public void handleEvent(HistoryEventRequest historyEventRequest) {
        try {
            amqpTemplate.convertAndSend(MessagingConfig.HISTORY_EXCHANGE_NAME, historyEventRequest);
        } catch (Exception e) {
            logger.error("Fail to send message to rabbit", e);
        }
    }
}