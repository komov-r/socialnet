package com.example.socialnetwork.model.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author RKomov
 */
@Table("Chat")
@Getter
@Setter
@AllArgsConstructor
public class Chat {

    @PrimaryKeyColumn(name = "ownerUserId", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    Long ownerUserId;

    @PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    String id;

    @PrimaryKeyColumn(name = "time", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    LocalDateTime time;

    @CassandraType(type = CassandraType.Name.LIST, typeArguments = { CassandraType.Name.DOUBLE } )
    List<Long> participants;

}
