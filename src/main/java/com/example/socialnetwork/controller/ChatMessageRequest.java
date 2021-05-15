package com.example.socialnetwork.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

/**
 * @author RKomov
 */
@Getter
@Setter
public class ChatMessageRequest {

    String chatId;

    Long participantId;

    @JsonIgnore
    Long authorId;



    String message;

}
