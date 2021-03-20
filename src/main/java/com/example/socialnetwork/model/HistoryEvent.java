package com.example.socialnetwork.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author RKomov
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HistoryEvent implements Serializable {

    private Long userId;
    @Setter
    private String userDescription;
    private Long objectId;
    @Setter
    private String objectDescription;
    private ObjectType objectType;
    private HistoryEventType eventType;
    private LocalDate eventDate = LocalDate.now();


}
