package com.example.socialnetwork.controller;

import com.example.socialnetwork.service.ChatService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author RKomov
 */
@RestController
@RequestMapping("/api")
public class ChatController {

    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/message")
    public ChatMessageInfo addMessage(@RequestBody ChatMessageRequest message) {
        message.setAuthorId(getCurrentUserProfileId());
        return chatService.addMessage(message);
    }

    @GetMapping("/chat")
    public List<ChatInfo> getChatsList() {
        return chatService.getChatsList(getCurrentUserProfileId());
    }

    @GetMapping("/chat/{id}")
    public ChatInfo getChat(@PathVariable("id") String id) {
        return chatService.getChatById(getCurrentUserProfileId(), id);
    }

    @GetMapping("/message")
    public List<ChatMessageInfo> getChatMessages(@RequestParam("chatId") String chatId) {
        return chatService.getChatMessages(chatId);
    }

    private Long getCurrentUserProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }

}
