package ru.neoflex.calculator.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CreditUtil {
    public static BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal numerator = monthlyRate
                .add(BigDecimal.ONE)
                .pow(term)
                .multiply(monthlyRate);
        BigDecimal denominator = monthlyRate
                .add(BigDecimal.ONE)
                .pow(term)
                .subtract(BigDecimal.ONE);

        BigDecimal payment = amount.multiply(numerator.divide(denominator, 10, RoundingMode.HALF_UP));

        return payment.setScale(2, RoundingMode.HALF_UP);
    }
}
