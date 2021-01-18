package com.example.socialnetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableCaching
public class SocialNetworkApplication {
    static Logger logger = LoggerFactory.getLogger(SocialNetworkApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SocialNetworkApplication.class, args);

        initDatabase(context);
    }

    private static void initDatabase(ConfigurableApplicationContext context) {
        DataSource bean = context.getBean(DataSource.class);

        try (Connection connection = bean.getConnection()) {

            String sql = new BufferedReader(new InputStreamReader(new ClassPathResource("db/schema.sql").getInputStream()))
                    .lines().collect(Collectors.joining("\n"));

            try (Statement statement = connection.createStatement()) {

                for (String s : sql.split(";")) {
                    statement.addBatch(s);
                }
                statement.executeBatch();
            }

        } catch (Exception e) {
            logger.error("Failed to init db", e);
        }
    }

}
