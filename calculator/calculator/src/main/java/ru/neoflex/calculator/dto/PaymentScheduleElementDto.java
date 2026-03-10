package ru.neoflex.calculator.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentScheduleElementDto {
    private Integer number;
    private LocalDate date;
    private BigDecimal totalPayment;
    private BigDecimal interestPayment;
    private BigDecimal debtPayment;
    private BigDecimal remainingDebt;
}
