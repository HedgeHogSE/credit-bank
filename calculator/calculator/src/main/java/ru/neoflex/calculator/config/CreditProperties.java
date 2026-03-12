package ru.neoflex.calculator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Getter
@Setter
@ConfigurationProperties(prefix = "credit")
@Component
public class CreditProperties {

    private Calculator calculator = new Calculator();

    @Getter
    @Setter
    public static class Calculator {
        private BigDecimal baseRate;
        private BigDecimal insuranceCost;
    }
}
