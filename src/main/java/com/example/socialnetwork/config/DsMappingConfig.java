package com.example.socialnetwork.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author RKomov
 */
@Component
@ConfigurationProperties(prefix = "db-mapping")
@Getter
@Setter
//@RefreshScope
public class DsMappingConfig {

    String master;
    String profiles;
    String friends;

    List<DatasourceProps> datasources;

    public DatasourceProps getMasterDs() {
        return getDs(master);
    }

    public DatasourceProps getProfilesDs() {
        return getDs(profiles);
    }

    public DatasourceProps getFriendsDs() {
        return getDs(friends);
    }

    private DatasourceProps getDs(String name) {
        return datasources.stream().filter(v -> v.getName().equals(name)).findFirst().orElseThrow();
    }
}

@Getter
@Setter
class DatasourceProps {
    String name;
    String url;
    String username;
    String password;
    String driverClassName;
}

