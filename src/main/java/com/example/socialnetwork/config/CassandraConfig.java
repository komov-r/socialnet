package com.example.socialnetwork.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import java.util.List;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {
 
    @Override
    protected String getKeyspaceName() {
        return "chat";
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        final CreateKeyspaceSpecification specification =
                CreateKeyspaceSpecification.createKeyspace("chat")
                        .ifNotExists()
                        .with(KeyspaceOption.DURABLE_WRITES, true)
                        .withSimpleReplication();
        return List.of(specification);
    }

    @Override
    protected List<String> getStartupScripts() {
        return List.of(
                "USE chat",
                "CREATE TABLE IF NOT EXISTS ChatMessage (" +
                        "chatId text," +
                        "time timestamp," +
                        "author double," +
                        "message text," +
                        "PRIMARY KEY ((chatId), time, author)" +
                        ") WITH CLUSTERING ORDER BY (time ASC)",
                "CREATE TABLE IF NOT EXISTS Chat (" +
                        "ownerUserId double," +
                        "id text," +
                        "time timestamp," +
                        "participants list<double>," +
                        "PRIMARY KEY ((ownerUserId), id, time)" +
                        ") WITH CLUSTERING ORDER BY (id DESC)"

        );
    }
}
