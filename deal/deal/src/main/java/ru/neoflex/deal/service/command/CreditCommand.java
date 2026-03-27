package ru.neoflex.deal.service.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.model.PaymentSchedule;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CreditCommand {

    private BigDecimal amount;

    private Integer term;

    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private BigDecimal psk;

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;

    private List<PaymentSchedule> paymentSchedule;
}
