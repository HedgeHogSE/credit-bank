package ru.neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Элемент графика платежей")
@Data
@Builder
public class PaymentScheduleElementDto {

    @Schema(description = "Номер платежа", example = "1")
    private Integer number;

    @Schema(description = "Дата платежа", example = "2026-03-18")
    private LocalDate date;

    @Schema(description = "Полная сумма платежа", example = "9025.20")
    private BigDecimal totalPayment;

    @Schema(description = "Выплата процентов", example = "1250.00")
    private BigDecimal interestPayment;

    @Schema(description = "Выплата основного долга", example = "7775.20")
    private BigDecimal debtPayment;

    @Schema(description = "Остаток основного долга", example = "92224.80")
    private BigDecimal remainingDebt;
}
