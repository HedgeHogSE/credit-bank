package ru.neoflex.calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.config.CreditProperties;
import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.PaymentScheduleElementDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.enums.Gender;
import ru.neoflex.calculator.enums.MaritalStatus;
import ru.neoflex.calculator.enums.Position;
import ru.neoflex.calculator.exception.ScoringException;
import ru.neoflex.calculator.generator.DateNowGenerator;
import ru.neoflex.calculator.util.CreditUtil;
import ru.neoflex.calculator.util.UtilBigDecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;
import static ru.neoflex.calculator.util.CreditUtil.getPaymentScheduleElements;
import static ru.neoflex.calculator.util.CreditUtil.getPsk;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditService {

        private final CreditProperties creditProperties;

        private final ScoringService scoringService;

        private final DateNowGenerator dateNowGenerator;

        public CreditDto getCredit(ScoringDataDto scoringDataDto) {

                log.info("Calculating credit parameters for amount: {}, term: {}", scoringDataDto.getAmount(),
                                scoringDataDto.getTerm());

                BigDecimal rate = scoringService.scoring(scoringDataDto);

                BigDecimal amount = scoringDataDto.getIsInsuranceEnabled()
                                ? scoringDataDto.getAmount().add(creditProperties.getCalculator().insuranceCost())
                                : scoringDataDto.getAmount();

                BigDecimal monthlyPayment = CreditUtil.calculateMonthlyPayment(
                                amount, rate, scoringDataDto.getTerm());

                List<PaymentScheduleElementDto> paymentScheduleElements = getPaymentScheduleElements(amount,
                                monthlyPayment, rate, scoringDataDto.getTerm(), dateNowGenerator.generate());

                BigDecimal psk = getPsk(scoringDataDto.getTerm(), amount, monthlyPayment);

                log.debug("Intermediate calculation step: rate={}, amount={}, monthlyPayment={}, psk={}", rate, amount,
                                monthlyPayment, psk);

                log.info("Credit calculation finished successfully. Returning CreditDto.");
                return CreditDto
                                .builder()
                                .amount(amount)
                                .term(scoringDataDto.getTerm())
                                .monthlyPayment(monthlyPayment)
                                .rate(rate)
                                .psk(psk)
                                .isInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
                                .isSalaryClient(scoringDataDto.getIsSalaryClient())
                                .paymentSchedule(paymentScheduleElements)
                                .build();
        }

}
