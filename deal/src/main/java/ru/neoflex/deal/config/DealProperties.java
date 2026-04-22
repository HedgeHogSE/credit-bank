package ru.neoflex.deal.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "deal")
@RequiredArgsConstructor
public class DealProperties {

    private final String calculatorUrl;
}
