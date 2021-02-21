package com.example.socialnetwork;

import com.example.socialnetwork.config.DsMappingConfig;
import com.example.socialnetwork.model.Gender;
import com.example.socialnetwork.model.UserProfile;
import com.example.socialnetwork.repository.UserProfileRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(DsMappingConfig.class)
public class SocialNetworkApplication {
    static Logger logger = LoggerFactory.getLogger(SocialNetworkApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SocialNetworkApplication.class, args);

        initDatabase(context);

        generateData(context);
    }

    @Bean
    public HttpTraceRepository htttpTraceRepository() {
        return new InMemoryHttpTraceRepository();
    }


    private static void generateData(ConfigurableApplicationContext context) {
        UserProfileRepository repository = context.getBean(UserProfileRepository.class);

        Integer proflesToGen = context.getEnvironment().getProperty("PROFILES_TO_GENERATE", Integer.class);

        if (proflesToGen == null || proflesToGen == 0) {
            return;
        }

        Faker faker = new Faker();

        logger.info("Generating {} values", proflesToGen);

        int batchSize = 10_000;
        for (int i = 0; i < proflesToGen / batchSize + 1; i++) {

            logger.info("Inserting batch {}", i);
            int top = Math.min(proflesToGen - i * batchSize, batchSize);

            List<UserProfile> profiles = IntStream.range(0, top).mapToObj(j -> new UserProfile(
                    faker.bothify(faker.name().username() + ".??????"),
                    faker.bothify("??????????"),
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.address().cityName(),
                    faker.date().birthday().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate(),
                    faker.demographic().sex().equals("Male") ? Gender.M : Gender.W,
                    faker.esports().game()
            )).collect(Collectors.toList());

            repository.bulkInsert(profiles);
        }
        logger.info("Data has been generated");
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
