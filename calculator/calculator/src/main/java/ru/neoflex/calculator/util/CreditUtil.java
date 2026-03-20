package ru.neoflex.calculator.util;

import ru.neoflex.calculator.dto.PaymentScheduleElementDto;
import ru.neoflex.calculator.generator.DateNowGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public static List<PaymentScheduleElementDto> getPaymentScheduleElements(
            BigDecimal totalAmount,
            BigDecimal monthlyPayment,
            BigDecimal rate,
            int term,
            LocalDate dateNow) {

        List<PaymentScheduleElementDto> paymentScheduleElements = new ArrayList<>();

        LocalDate date = dateNow.plusMonths(1);

        BigDecimal remainingDebt = totalAmount;

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        for (int i = 1; i <= term; i++) {
            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal debtPayment;
            BigDecimal totalPayment;

            if (i == term) {
                debtPayment = remainingDebt;
                totalPayment = debtPayment.add(interestPayment);
                remainingDebt = BigDecimal.ZERO;
            } else {
                debtPayment = monthlyPayment.subtract(interestPayment)
                        .setScale(2, RoundingMode.HALF_UP);
                totalPayment = monthlyPayment;
                remainingDebt = remainingDebt.subtract(debtPayment)
                        .setScale(2, RoundingMode.HALF_UP);
            }

            paymentScheduleElements.add(
                    PaymentScheduleElementDto.builder()
                            .number(i)
                            .date(date)
                            .totalPayment(totalPayment)
                            .interestPayment(interestPayment)
                            .debtPayment(debtPayment)
                            .remainingDebt(remainingDebt)
                            .build()
            );
            date = date.plusMonths(1);
        }

        return paymentScheduleElements;
    }

    public static BigDecimal getPsk(int term, BigDecimal totalAmount, BigDecimal monthlyPayment) {

        BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(term));

        return totalPayment.subtract(totalAmount).divide(totalAmount, 2, RoundingMode.HALF_UP);
    }
}
