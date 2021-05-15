package com.example.socialnetwork.controller;

import lombok.Getter;

/**
 * @author RKomov
 */
@Getter
public class ChatParticipantInfo {

    Long userId;
    String name;

    public ChatParticipantInfo(Long userId) {
        this.userId = userId;
    }

    public ChatParticipantInfo(Long userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
