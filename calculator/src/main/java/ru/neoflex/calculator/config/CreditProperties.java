package ru.neoflex.calculator.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "credit")
public class CreditProperties {

    private final Calculator calculator;

    public record Calculator(
            BigDecimal baseRate,
            BigDecimal insuranceCost) {}
}
