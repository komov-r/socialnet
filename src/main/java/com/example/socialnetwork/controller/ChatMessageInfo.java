package com.example.socialnetwork.controller;

import com.example.socialnetwork.controller.api.UserProfileInfo;
import com.example.socialnetwork.model.chat.ChatMessage;

import java.time.LocalDateTime;

/**
 * @author RKomov
 */

public class ChatMessageInfo {
    ChatMessage message;

    ChatParticipantInfo author;

    public String getChatId() {
        return message.getChatId();
    }

    public LocalDateTime getTime() {
        return message.getTime();
    }

    public String getMessage() {
        return message.getMessage();
    }

    public ChatParticipantInfo getAuthor() {
        return author;
    }

    public ChatMessageInfo(ChatMessage message, UserProfileInfo author) {
        this.message = message;
        this.author = new ChatParticipantInfo(message.getAuthor(), author == null ? "anon" : author.getFullName());
    }
}
