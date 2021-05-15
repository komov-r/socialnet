package com.example.socialnetwork.model.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author RKomov
 */
@Getter
@Setter
@Table("ChatMessage")
@AllArgsConstructor
public class ChatMessage {

    @PrimaryKeyColumn(name = "chatId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    String chatId;

    @PrimaryKeyColumn(name = "time", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    LocalDateTime time;

    @PrimaryKeyColumn(name = "author", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    Long author;

    String message;
}
