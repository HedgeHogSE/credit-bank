package ru.neoflex.calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.config.CreditProperties;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.generator.DefaultUuidGenerator;
import ru.neoflex.calculator.util.CreditUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanOffersService {

    private final CreditProperties creditProperties;

    private final DefaultUuidGenerator uuidGenerator;

    public List<LoanOfferDto> getOffers(LoanStatementRequestDto request) {

        log.info("Generating loan offers for amount: {}, term: {}", request.getAmount(), request.getTerm());

        UUID statementId = uuidGenerator.generate();

        BigDecimal baseRate = creditProperties.getCalculator().baseRate();
        BigDecimal insuranceCost = creditProperties.getCalculator().insuranceCost();

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
                        request.getTerm());

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

                log.debug(
                        "Generated offer: rate={}, totalAmount={}, monthlyPayment={}, isInsuranceEnabled={}, isSalaryClient={}",
                        rate, totalAmount, monthlyPayment, isInsuranceEnabled, isSalaryClient);

                offers.add(offer);
            }
        }

        offers.sort(Comparator.comparing(LoanOfferDto::getRate));
        log.info("Generated {} loan offers successfully.", offers.size());

        return offers;
    }
}
