package com.example.socialnetwork.event.rabbit;

import com.example.socialnetwork.config.MessagingConfig;
import com.example.socialnetwork.event.HistoryEventListener;
import com.example.socialnetwork.event.HistoryEventRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author RKomov
 */
@Component
public class RabbitMessagesReceiver {

    private HistoryEventListener historyEventListener;

    public RabbitMessagesReceiver(HistoryEventListener historyEventListener) {
        this.historyEventListener = historyEventListener;
    }

    @RabbitListener(queues = MessagingConfig.QUEUE_NAME)
    public void receiveHistoryEvent(HistoryEventRequest historyEventRequest) {
        historyEventListener.handleEvent(historyEventRequest);
    }
}
