package com.example.socialnetwork.model;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author RKomov
 */
@Getter
public class HistoryItem implements Serializable {

    private Long id;

    private Long ownerId;
    private HistoryEvent event;


    public HistoryItem() {
    }

    public HistoryItem(Long id,
                       Long ownerId,
                       HistoryEvent event) {
        this.id = id;
        this.ownerId = ownerId;
        this.event = event;
    }
}
