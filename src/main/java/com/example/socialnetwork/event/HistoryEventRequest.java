package com.example.socialnetwork.event;

import com.example.socialnetwork.model.HistoryEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author RKomov
 */
@Getter
@AllArgsConstructor
public class HistoryEventRequest {
    HistoryEvent event;
    int limit;
    Long minId;

    public HistoryEventRequest() {
    }

    public HistoryEventRequest(HistoryEvent event, int limit) {
        this.event = event;
        this.limit = limit;
    }
}
