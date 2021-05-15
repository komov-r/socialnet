package com.example.socialnetwork.repository;

import com.example.socialnetwork.model.chat.Chat;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

/**
 * @author RKomov
 */
public interface ChatRepository extends CassandraRepository<Chat, String> {


    List<Chat> findByOwnerUserId(Long userId);

    List<Chat> findByOwnerUserIdAndId(Long userId, String chatId);
}
