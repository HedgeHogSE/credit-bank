package ru.neoflex.calculator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class LoanOffersService {

    @Value("${credit.base-rate}")
    private BigDecimal baseRate;

    @Value("${credit.insurance-cost}")
    private BigDecimal insuranceCost;

    public List<LoanOfferDto> createOffers(LoanStatementRequestDto request) {

        UUID statementId = UUID.randomUUID();
        List<LoanOfferDto> offers = new ArrayList<>();

        for (boolean isInsuranceEnabled : List.of(false, true)) {
            for (boolean isSalaryClient : List.of(false, true)) {

                BigDecimal rate = baseRate;
                BigDecimal totalAmount = request.getAmount();

                if (isInsuranceEnabled) {
                    rate = rate.subtract(BigDecimal.valueOf(0.03));
                    totalAmount = totalAmount.add(insuranceCost);
                }

                if (isSalaryClient) {
                    rate = rate.subtract(BigDecimal.valueOf(0.01));
                }

                BigDecimal monthlyPayment = calculateMonthlyPayment(
                        totalAmount,
                        rate,
                        request.getTerm()
                );

                LoanOfferDto offer = LoanOfferDto.builder()
                        .statementId(statementId)
                        .requestedAmount(request.getAmount())
                        .totalAmount(totalAmount)
                        .term(request.getTerm())
                        .monthlyPayment(monthlyPayment)
                        .rate(rate)
                        .isInsuranceEnabled(isInsuranceEnabled)
                        .isSalaryClient(isSalaryClient)
                        .build();

                offers.add(offer);
            }
        }

        offers.sort(Comparator.comparing(LoanOfferDto::getRate));

        return offers;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {

        double monthlyRate = rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP).doubleValue();

        double numerator = monthlyRate * Math.pow(1 + monthlyRate, term);
        double denominator = Math.pow(1 + monthlyRate, term) - 1;

        double payment = amount.doubleValue() * (numerator / denominator);

        return BigDecimal.valueOf(payment).setScale(2, RoundingMode.HALF_UP);
    }
}
