package ru.neoflex.statement.service.command;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
public class LoanOfferCommand {

    private UUID statementId;

    private BigDecimal requestedAmount;

    private BigDecimal totalAmount;

    private Integer term;

    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;
}
