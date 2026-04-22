package ru.neoflex.statement.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "statement")
@RequiredArgsConstructor
public class StatementProperties {

    private final String dealUrl;
}
