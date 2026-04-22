package ru.neoflex.calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.calculator.config.CreditProperties;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.enums.Gender;
import ru.neoflex.calculator.enums.MaritalStatus;
import ru.neoflex.calculator.enums.Position;
import ru.neoflex.calculator.exception.ScoringException;
import ru.neoflex.calculator.generator.DateNowGenerator;
import ru.neoflex.calculator.util.UtilBigDecimal;

import java.math.BigDecimal;
import java.time.Period;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScoringService {

    private final CreditProperties creditProperties;

    private final DateNowGenerator dateNowGenerator;

    public BigDecimal scoring(ScoringDataDto scoringDataDto) {

        log.info("Scoring started for user {} {}", scoringDataDto.getFirstName(), scoringDataDto.getLastName());

        BigDecimal rate = creditProperties.getCalculator().baseRate();

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

        if (UtilBigDecimal.isGreaterThan(
                scoringDataDto.getAmount(),
                scoringDataDto.getEmployment().getSalary().multiply(BigDecimal.valueOf(24)))) {
            throw new ScoringException(
                    "Refusal! Loans are not be issued if 24 times your salary is less than the loan amount.");
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

        int age = Period.between(scoringDataDto.getBirthdate(), dateNowGenerator.generate()).getYears();

        if (age < 20 || age > 65) {
            throw new ScoringException(
                    "Refusal! Loans are not issued to individuals under 20 or over 65 years of age.");
        }

        if (scoringDataDto.getGender() == Gender.FEMALE && (age >= 32 && age <= 60)) {
            rate = rate.subtract(BigDecimal.valueOf(0.03));
        }
        if (scoringDataDto.getGender() == Gender.MALE && (age >= 30 && age <= 55)) {
            rate = rate.subtract(BigDecimal.valueOf(0.03));
        }

        if (scoringDataDto.getEmployment().getWorkExperienceTotal() < 18
                || scoringDataDto.getEmployment().getWorkExperienceCurrent() < 3) {
            throw new ScoringException("""
                    Refusal! Loans are not issued to individuals with less than 18 months of total \
                    employment history or less than 3 months of current employment history.""");
        }

        if (scoringDataDto.getIsInsuranceEnabled()) {
            rate = rate.subtract(BigDecimal.valueOf(0.03));
        }
        if (scoringDataDto.getIsSalaryClient()) {
            rate = rate.subtract(BigDecimal.valueOf(0.01));
        }

        log.info("Scoring completed successfully. Final calculated rate is {}", rate);
        return rate;
    }
}
