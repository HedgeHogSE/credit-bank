package ru.neoflex.deal.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.model.PaymentSchedule;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Полная информация о выданном кредите")
@Data
@Builder
public class CreditDto {

    @Schema(description = "Сумма кредита (с учетом страховки, если применимо)", example = "100000")
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "12")
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "9025.20")
    private BigDecimal monthlyPayment;

    @Schema(description = "Процентная ставка (в год)", example = "15")
    private BigDecimal rate;

    @Schema(description = "Полная стоимость кредита (ПСК) в процентах", example = "16.12")
    private BigDecimal psk;

    @Schema(description = "Включена ли страховка", example = "false")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Является ли клиент зарплатным", example = "false")
    private Boolean isSalaryClient;

    @Schema(description = "График платежей по кредиту")
    private List<PaymentSchedule> paymentSchedule;
}
