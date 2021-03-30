package com.example.socialnetwork.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author RKomov
 */
@Configuration
public class DsConfig {

    Logger log = LoggerFactory.getLogger(DsConfig.class);


    private org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope;


    public DsConfig(org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope) {
        this.refreshScope = refreshScope;
    }

    @Bean
    @Primary
    @RefreshScope
    public DataSource primaryDataSource(DsMappingConfig dsMappingConfig) {
        log.debug("Creating primaryDataSource");
        DatasourceProps ds = dsMappingConfig.getMasterDs();

        return createDs(ds.getUrl(),
                ds.getUsername(),
                ds.getPassword());
    }

    @Bean
    @Qualifier("profilesDs")
    @RefreshScope
    public DataSource profilesDs(DsMappingConfig dsMappingConfig) {
        log.debug("Creating  profilesDs");
        DatasourceProps ds = dsMappingConfig.getProfilesDs();

        return createDs(ds.getUrl(),
                ds.getUsername(),
                ds.getPassword());
    }

    @Bean
    @Qualifier("friendsDs")
    @RefreshScope
    public DataSource friendsDs(DsMappingConfig dsMappingConfig) {
        log.debug("Creating friendsDs");
        DatasourceProps ds = dsMappingConfig.getFriendsDs();

        return createDs(ds.getUrl(),
                ds.getUsername(),
                ds.getPassword());
    }

//    @Bean
//    @Qualifier("friendsTx")
//    public JdbcTransactionManager friendsTx() {
//        return new JdbcTransactionManager(friendsDs());
//    }

    private DataSource createDs(String jdbcUrl, String username, String password) {
        final HikariConfig config = new HikariConfig();
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(50);
        config.setConnectionTimeout(10000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

}
