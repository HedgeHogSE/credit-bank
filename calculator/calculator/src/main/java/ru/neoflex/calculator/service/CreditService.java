package ru.neoflex.calculator.service;

import lombok.RequiredArgsConstructor;
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
import ru.neoflex.calculator.util.CreditUtil;
import ru.neoflex.calculator.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;


@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditProperties creditProperties;

    public CreditDto getCredit(ScoringDataDto scoringDataDto) {

        BigDecimal amount = scoringDataDto.getIsInsuranceEnabled() ?
                scoringDataDto.getAmount().add(creditProperties.getCalculator().getInsuranceCost()) :
                scoringDataDto.getAmount();

        BigDecimal rate = scoring(scoringDataDto);

        BigDecimal monthlyPayment = CreditUtil.calculateMonthlyPayment(
                amount, rate, scoringDataDto.getTerm());

        List<PaymentScheduleElementDto> paymentScheduleElements =
                getPaymentScheduleElements(amount, monthlyPayment, rate, scoringDataDto.getTerm());

        BigDecimal psk = getPsk(scoringDataDto.getTerm(), amount, monthlyPayment);

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

    public BigDecimal scoring(ScoringDataDto scoringDataDto) {

        BigDecimal rate = creditProperties.getCalculator().getBaseRate();

        if (scoringDataDto.getEmployment().getEmploymentStatus() == EmploymentStatus.UNEMPLOYED) {
            throw new ScoringException("Refusal! Loans are not issued to the unemployed.");
        } else if (scoringDataDto.getEmployment().getEmploymentStatus() == EmploymentStatus.EMPLOYED) {
            rate = rate.subtract(BigDecimal.valueOf(0.01));
        } else if (scoringDataDto.getEmployment().getEmploymentStatus() == EmploymentStatus.BUSINESS_OWNER) {
            rate = rate.add(BigDecimal.valueOf(0.01));
        } else if (scoringDataDto.getEmployment().getEmploymentStatus() == EmploymentStatus.SELF_EMPLOYED) {
            rate = rate.add(BigDecimal.valueOf(0.02));
        }

        if (scoringDataDto.getEmployment().getPosition() == Position.WORKER) {
            rate = rate.subtract(BigDecimal.valueOf(0.01));
        } else if (scoringDataDto.getEmployment().getPosition() == Position.MID_MANAGER) {
            rate = rate.subtract(BigDecimal.valueOf(0.02));
        } else if (scoringDataDto.getEmployment().getPosition() == Position.TOP_MANAGER) {
            rate = rate.subtract(BigDecimal.valueOf(0.03));
        }

        if (Util.isGreaterThan(
                scoringDataDto.getAmount(),
                scoringDataDto.getEmployment().getSalary().multiply(BigDecimal.valueOf(24)))
        ) {
            throw new ScoringException("Refusal! Loans are not be issued if 24 times your salary is less than the loan amount.");
        }

        if (scoringDataDto.getMaritalStatus() == MaritalStatus.MARRIED) {
            rate = rate.subtract(BigDecimal.valueOf(0.03));
        } else if (scoringDataDto.getMaritalStatus() == MaritalStatus.SINGLE) {
            rate = rate.subtract(BigDecimal.valueOf(0.01));
        } else if (scoringDataDto.getMaritalStatus() == MaritalStatus.DIVORCED) {
            rate = rate.add(BigDecimal.valueOf(0.01));
        } else if (scoringDataDto.getMaritalStatus() == MaritalStatus.WIDOWED) {
            rate = rate.add(BigDecimal.valueOf(0.02));
        }

        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();

        if (age < 20 || age > 65) {
            throw new ScoringException("Refusal! Loans are not issued to individuals under 20 or over 65 years of age.");
        }

        if (scoringDataDto.getGender() == Gender.FEMALE && (age >= 32 && age <= 60)) {
            rate = rate.subtract(BigDecimal.valueOf(0.03));
        }
        if (scoringDataDto.getGender() == Gender.MALE && (age >= 30 && age <= 55)) {
            rate = rate.subtract(BigDecimal.valueOf(0.03));
        }

        if (scoringDataDto.getEmployment().getWorkExperienceTotal() < 18 || scoringDataDto.getEmployment().getWorkExperienceCurrent() < 3) {
            throw new ScoringException("""
                    Refusal! Loans are not issued to individuals with less than 18 months of total \
                    employment history or less than 3 months of current employment history."""
            );
        }

        if (scoringDataDto.getIsInsuranceEnabled()) {
            rate = rate.subtract(BigDecimal.valueOf(0.03));
        }
        if (scoringDataDto.getIsSalaryClient()) {
            rate = rate.subtract(BigDecimal.valueOf(0.01));
        }

        return rate;
    }

    public List<PaymentScheduleElementDto> getPaymentScheduleElements(
            BigDecimal totalAmount,
            BigDecimal monthlyPayment,
            BigDecimal rate,
            int term) {

        List<PaymentScheduleElementDto> paymentScheduleElements = new ArrayList<>();

        LocalDate date = LocalDate.now().plusMonths(1);

        BigDecimal remainingDebt = totalAmount;

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        for (int i = 1; i <= term; i++) {

            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate);
            interestPayment = interestPayment.setScale(2, RoundingMode.HALF_UP);

            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            debtPayment = debtPayment.setScale(2, RoundingMode.HALF_UP);

            remainingDebt = remainingDebt.subtract(debtPayment);
            remainingDebt = remainingDebt.setScale(2, RoundingMode.HALF_UP);

            paymentScheduleElements.add(
                    PaymentScheduleElementDto.builder()
                            .number(i)
                            .date(date)
                            .totalPayment(monthlyPayment)
                            .interestPayment(interestPayment)
                            .debtPayment(debtPayment)
                            .remainingDebt(remainingDebt)
                            .build()
            );

            date = date.plusMonths(1);
        }

        return paymentScheduleElements;
    }

    public BigDecimal getPsk(int term, BigDecimal totalAmount, BigDecimal monthlyPayment) {

        BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(term));

        return totalPayment.subtract(totalAmount).divide(totalAmount, 2, RoundingMode.HALF_UP);
    }
}
