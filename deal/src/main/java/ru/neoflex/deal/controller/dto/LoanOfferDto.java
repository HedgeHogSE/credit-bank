package ru.neoflex.deal.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Кредитное предложение для клиента")
@Data
@Builder
public class LoanOfferDto {

    @Schema(description = "Уникальный идентификатор заявки", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID statementId;

    @Schema(description = "Запрошенная сумма", example = "20000")
    private BigDecimal requestedAmount;

    @Schema(description = "Общая сумма кредита (с учетом страховки)", example = "120000")
    private BigDecimal totalAmount;

    @Schema(description = "Срок кредита (в месяцах)", example = "12")
    private Integer term;

    @Schema(description = "Ежемесячный платеж", example = "11050")
    private BigDecimal monthlyPayment;

    @Schema(description = "Итоговая процентная ставка", example = "15.0")
    private BigDecimal rate;

    @Schema(description = "Указатель наличия страховки (снижает ставку)", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Указатель зарплатного клиента (снижает ставку)", example = "false")
    private Boolean isSalaryClient;
}