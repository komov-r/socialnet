package com.example.socialnetwork.controller;

import com.example.socialnetwork.controller.api.UserProfileInfo;
import com.example.socialnetwork.model.chat.Chat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author RKomov
 */
@Getter
@Setter
public class ChatInfo {

    Chat chat;

    List<ChatParticipantInfo> participants;

    public String getId() {
        return chat.getId();
    }

    public LocalDateTime getTime() {
        return chat.getTime();
    }

    public List<ChatParticipantInfo> getParticipants() {
        return participants;
    }

    public ChatInfo(Chat chat, List<UserProfileInfo> participants) {
        this.chat = chat;
        this.participants = participants.stream()
                .map(v -> new ChatParticipantInfo(v.getId(), v.getFullName()))
                .collect(Collectors.toList());
    }
}
