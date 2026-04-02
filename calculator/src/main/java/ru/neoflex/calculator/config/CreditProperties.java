package ru.neoflex.calculator.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

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
