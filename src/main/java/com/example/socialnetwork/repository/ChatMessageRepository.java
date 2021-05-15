package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.chat.ChatMessage;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

/**
 * @author RKomov
 */
public interface ChatMessageRepository extends CassandraRepository<ChatMessage, String> {

    List<ChatMessage> findByChatId(String chatId);

}
