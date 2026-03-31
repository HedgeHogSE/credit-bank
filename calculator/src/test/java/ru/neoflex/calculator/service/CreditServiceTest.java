package ru.neoflex.calculator.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.calculator.config.CreditProperties;
import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.generator.DateNowGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

    @Mock
    private CreditProperties creditProperties;

    @Mock
    private CreditProperties.Calculator calculator;

    @Mock
    private ScoringService scoringService;

    @Mock
    private DateNowGenerator dateNowGenerator;

    @InjectMocks
    private CreditService creditService;

    @BeforeEach
    void setUp() {

        when(dateNowGenerator.generate()).thenReturn(LocalDate.parse("2026-03-13"));
    }

    @ParameterizedTest
    @CsvSource({
            "true, 200000",
            "false, 100000"
    })
    void getCreditShouldReturnFullCreditDto(boolean isInsuranceEnabled, BigDecimal expected) {

        if (isInsuranceEnabled) {
            when(calculator.insuranceCost()).thenReturn(BigDecimal.valueOf(100000));
            when(creditProperties.getCalculator()).thenReturn(calculator);
        }

        ScoringDataDto request = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(true)
                .build();

        BigDecimal resultRate = BigDecimal.valueOf(0.10);

        when(scoringService.scoring(request)).thenReturn(resultRate);

        CreditDto result = creditService.getCredit(request);

        assertEquals(expected, result.getAmount());

        assertEquals(resultRate, result.getRate());

        assertEquals(12, result.getPaymentSchedule().size());
    }
}
