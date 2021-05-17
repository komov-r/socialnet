package com.example.socialnetwork.service;

import com.example.socialnetwork.controller.ChatInfo;
import com.example.socialnetwork.controller.ChatMessageInfo;
import com.example.socialnetwork.controller.ChatMessageRequest;
import com.example.socialnetwork.controller.api.UserProfileInfo;
import com.example.socialnetwork.model.chat.Chat;
import com.example.socialnetwork.model.chat.ChatMessage;
import com.example.socialnetwork.repository.ChatMessageRepository;
import com.example.socialnetwork.repository.ChatRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author RKomov
 */
@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRepository chatRepository;

    private final ProfileService profileService;
    private final SimpMessagingTemplate template;

    public ChatService(ChatMessageRepository chatMessageRepository,
                       ChatRepository chatRepository, ProfileService profileService,
                       SimpMessagingTemplate template) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRepository = chatRepository;
        this.profileService = profileService;
        this.template = template;
    }

    public ChatMessageInfo addMessage(ChatMessageRequest message) {

        Chat chat = getChat(message);

        ChatMessage msg = new ChatMessage(chat.getId(), LocalDateTime.now(), message.getAuthorId(), message.getMessage());

        chatMessageRepository.save(msg);

        ChatMessageInfo msgInfo = toInfo(msg);

        chat.getParticipants().stream()
                .filter(v -> !v.equals(message.getAuthorId()))
                .forEach(c -> sendMessage(msgInfo, c));

        return msgInfo;
    }

    private void sendMessage(ChatMessageInfo msgInfo, Long c) {
        template.convertAndSendToUser(c.toString(), "/queue/message", msgInfo);
    }

    private Chat getChat(ChatMessageRequest message) {
        Chat chat;
        if (message.getChatId() == null) {
            String newChatId = newChatId();

            chat = new Chat(message.getAuthorId(), newChatId, LocalDateTime.now(), List.of(message.getAuthorId(), message.getParticipantId()));

            chatRepository.save(chat);
            chatRepository.save(new Chat(message.getParticipantId(), newChatId, LocalDateTime.now(), List.of(message.getAuthorId(), message.getParticipantId())));

        } else {
            chat = chatRepository.findByOwnerUserIdAndId(message.getAuthorId(), message.getChatId()).stream()
                    .findFirst().orElseThrow();
        }
        return chat;
    }

    private String newChatId() {
        return com.datastax.oss.driver.api.core.uuid.Uuids.timeBased().toString();
    }


    public List<ChatInfo> getChatsList(Long currentUserProfileId) {
        return chatRepository.findByOwnerUserId(currentUserProfileId).stream()
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    public ChatInfo getChatById(Long ownerId, String id) {
        return chatRepository.findByOwnerUserIdAndId(ownerId, id).stream()
                .findFirst()
                .map(this::toInfo)
                .orElseThrow();
    }

    public List<ChatMessageInfo> getChatMessages(String chatId) {
        return chatMessageRepository.findByChatId(chatId).stream()
                .map(this::toInfo)
                .collect(Collectors.toList());
    }

    private ChatMessageInfo toInfo(ChatMessage chatMessage) {
        return new ChatMessageInfo(chatMessage, profileService.getById(chatMessage.getAuthor()).orElse(null));
    }

    private ChatInfo toInfo(Chat chat) {
        List<UserProfileInfo> profiles = chat.getParticipants().stream().map(profileService::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return new ChatInfo(chat, profiles);
    }

}