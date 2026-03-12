package ru.neoflex.calculator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.config.CreditProperties;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.util.CreditUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanOffersService {

    private final CreditProperties creditProperties;

    public List<LoanOfferDto> createOffers(LoanStatementRequestDto request) {

        UUID statementId = UUID.randomUUID(); // чета сделать с этим

        BigDecimal baseRate = creditProperties.getCalculator().getBaseRate();
        BigDecimal insuranceCost = creditProperties.getCalculator().getInsuranceCost();

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

                BigDecimal monthlyPayment = CreditUtil.calculateMonthlyPayment(
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


}
