package ru.neoflex.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.calculator.config.CreditProperties;
import ru.neoflex.calculator.dto.EmploymentDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.enums.Gender;
import ru.neoflex.calculator.enums.MaritalStatus;
import ru.neoflex.calculator.enums.Position;
import ru.neoflex.calculator.exception.ScoringException;
import ru.neoflex.calculator.generator.DateNowGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScoringServiceTest {

    @Mock
    private CreditProperties creditProperties;

    @Mock
    private CreditProperties.Calculator calculator;

    @Mock
    private DateNowGenerator dateNowGenerator;

    @InjectMocks
    private ScoringService scoringService;

    @BeforeEach
    void setUp() {
        when(calculator.baseRate()).thenReturn(new BigDecimal("0.15"));
        when(creditProperties.getCalculator()).thenReturn(calculator);
    }

    @Test
    void scoringShouldThrowExceptionWhenUnemployed() {
        ScoringDataDto scoringDataDto = createClient();
        scoringDataDto.getEmployment().setEmploymentStatus(EmploymentStatus.UNEMPLOYED);

        assertThrows(ScoringException.class, () -> scoringService.scoring(scoringDataDto));
    }

    @Test
    void scoringShouldThrowExceptionWhenAmountMoreThan24MonthsSalary() {
        ScoringDataDto scoringDataDto = createClient();
        scoringDataDto.setAmount(BigDecimal.valueOf(10_000_000));
        scoringDataDto.getEmployment().setSalary(BigDecimal.valueOf(10_000));

        assertThrows(ScoringException.class, () -> scoringService.scoring(scoringDataDto));
    }

    @ParameterizedTest
    @CsvSource({"12", "19", "66", "74"})
    void scoringShouldThrowExceptionWhenAgeLessThan20OrMoreThan65(int age) {
        LocalDate dateNow = LocalDate.parse("2026-03-13");
        when(dateNowGenerator.generate()).thenReturn(dateNow);

        ScoringDataDto scoringDataDto = createClient();
        scoringDataDto.setBirthdate(dateNow.minusYears(age));

        assertThrows(ScoringException.class, () -> scoringService.scoring(scoringDataDto));
    }

    @ParameterizedTest
    @CsvSource({
            "12, 1",
            "17, 2",
            "20, 2",
            "17, 4"
    })
    void scoringShouldThrowExceptionWhenWorkExperienceTotalLessThan18_OrWorkExperienceCurrentLessThan3(
            int workExperienceTotal,
            int workExperienceCurrent) {
        when(dateNowGenerator.generate()).thenReturn(LocalDate.parse("2026-03-13"));

        ScoringDataDto scoringDataDto = createClient();
        scoringDataDto.getEmployment().setWorkExperienceTotal(workExperienceTotal);
        scoringDataDto.getEmployment().setWorkExperienceCurrent(workExperienceCurrent);

        assertThrows(ScoringException.class, () -> scoringService.scoring(scoringDataDto));
    }

    @ParameterizedTest
    @CsvSource({
            "EMPLOYED, WORKER, MARRIED, 0.15, 0.07", // 0.15 - 0.01 - 0.01 - 0.03 - 0.03 = 0.07
            "BUSINESS_OWNER, MID_MANAGER, SINGLE, 0.15, 0.10", // 0.15 + 0.01 - 0.02 - 0.01 - 0.03 = 0.10
            "SELF_EMPLOYED, TOP_MANAGER, DIVORCED, 0.15, 0.12", // 0.15 + 0.02 - 0.03 + 0.01 - 0.03 = 0.12
            "SELF_EMPLOYED, TOP_MANAGER, WIDOWED, 0.15, 0.13" // 0.15 + 0.02 - 0.03 + 0.02 - 0.03 = 0.13

    })
    void scoringShouldCalculateCorrectRate(
            EmploymentStatus status,
            Position position,
            MaritalStatus marital,
            BigDecimal baseRate,
            BigDecimal expectedRate) {

        when(calculator.baseRate()).thenReturn(baseRate);
        when(dateNowGenerator.generate()).thenReturn(LocalDate.parse("2026-03-13"));

        ScoringDataDto data = createClient();
        data.getEmployment().setEmploymentStatus(status);
        data.getEmployment().setPosition(position);
        data.setMaritalStatus(marital);

        BigDecimal result = scoringService.scoring(data);

        assertEquals(0, expectedRate.compareTo(result));
    }

    @ParameterizedTest
    @CsvSource({
            "MALE, 30, 0.15, 0.09", // 0.15 - 0.01 - 0.01 - 0.01 - 0.03 = 0.09
            "FEMALE, 32, 0.15, 0.09", // 0.15 - 0.01 - 0.01 - 0.01 - 0.03 = 0.09
            "MALE, 25, 0.15, 0.12", // 0.15 - 0.01 - 0.01 - 0.01 = 0.12
            "FEMALE, 25, 0.15, 0.12" // 0.15 - 0.01 - 0.01 - 0.01 = 0.12

    })
    void scoringShouldCalculateCorrectRateBasedOnAgeAndGender(
            Gender gender,
            int age,
            BigDecimal baseRate,
            BigDecimal expectedRate) {

        LocalDate dateNow = LocalDate.parse("2026-03-13");

        when(calculator.baseRate()).thenReturn(baseRate);
        when(dateNowGenerator.generate()).thenReturn(dateNow);

        ScoringDataDto data = createClient();
        data.setGender(gender);
        data.setBirthdate(dateNow.minusYears(age));

        BigDecimal result = scoringService.scoring(data);

        assertEquals(0, expectedRate.compareTo(result));
    }

    @ParameterizedTest
    @CsvSource({
            "true, true, 0.15, 0.05", // 0.15 - 0.01 - 0.01 - 0.01 - 0.03 - 0.03 - 0.01 = 0.05
            "true, false, 0.15, 0.06", // 0.15 - 0.01 - 0.01 - 0.01 - 0.03 - 0.03 = 0.06
            "false, true, 0.15, 0.08", // 0.15 - 0.01 - 0.01 - 0.01 - 0.03 - 0.01 = 0.08
            "false, false, 0.15, 0.09" // 0.15 - 0.01 - 0.01 - 0.01 - 0.03 = 0.09

    })
    void scoringShouldCalculateCorrectRateBasedOnInsuranceEnabledAndSalaryClient(
            boolean isInsuranceEnabled,
            boolean isSalaryClient,
            BigDecimal baseRate,
            BigDecimal expectedRate) {

        when(calculator.baseRate()).thenReturn(baseRate);
        when(dateNowGenerator.generate()).thenReturn(LocalDate.parse("2026-03-13"));

        ScoringDataDto data = createClient();

        data.setIsInsuranceEnabled(isInsuranceEnabled);
        data.setIsSalaryClient(isSalaryClient);

        BigDecimal result = scoringService.scoring(data);

        assertEquals(0, expectedRate.compareTo(result));
    }

    private ScoringDataDto createClient() {

        return ScoringDataDto.builder()
                .amount(new BigDecimal("100000"))
                .term(12)
                .birthdate(LocalDate.now().minusYears(40))
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.SINGLE) // -0.01
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED) // -0.01
                        .position(Position.WORKER) // -0.01
                        .salary(new BigDecimal("50000"))
                        .workExperienceTotal(24)
                        .workExperienceCurrent(12)
                        .build())
                .build();
    }
}
