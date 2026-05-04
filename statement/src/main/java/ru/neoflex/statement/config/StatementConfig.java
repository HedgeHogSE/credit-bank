package ru.neoflex.statement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.time.Clock;

@Configuration
@RequiredArgsConstructor
public class StatementConfig {

    private final StatementProperties statementProperties;

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(statementProperties.getDealUrl())
                .build();
    }
}
