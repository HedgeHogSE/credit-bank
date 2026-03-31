package ru.neoflex.calculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.calculator.config.CreditProperties;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.generator.DefaultUuidGenerator;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanOffersServiceTest {

    @Mock
    private CreditProperties creditProperties;

    @Mock
    private DefaultUuidGenerator uuidGenerator;

    @Mock
    private CreditProperties.Calculator calculator;

    @InjectMocks
    private LoanOffersService loanOffersService;

    @BeforeEach
    void setup() {
        when(calculator.baseRate()).thenReturn(BigDecimal.valueOf(0.15));
        when(calculator.insuranceCost()).thenReturn(BigDecimal.valueOf(100_000));
        when(creditProperties.getCalculator()).thenReturn(calculator);
    }

    @Test
    void getOffersShouldContainsCorrectRateAndFourOffers() {

        UUID uuid = UUID.randomUUID();

        when(uuidGenerator.generate()).thenReturn(uuid);

        LoanStatementRequestDto loanStatementRequestDto =
                LoanStatementRequestDto
                        .builder()
                        .amount(BigDecimal.valueOf(500_000))
                        .term(12)
                        .build();

        List<LoanOfferDto> result = loanOffersService.getOffers(loanStatementRequestDto);

        assertEquals(BigDecimal.valueOf(0.11), result.getFirst().getRate()); // лучшая ставка будет первой в листе
        assertEquals(BigDecimal.valueOf(0.15), result.getLast().getRate()); // Худшая ставка будет последней в листе

        assertEquals(4, result.size());
    }

    @Test
    void getOffersShouldContainsMustContainMoreAmountWhenInsuring() {

        UUID uuid = UUID.randomUUID();

        when(uuidGenerator.generate()).thenReturn(uuid);

        LoanStatementRequestDto loanStatementRequestDto =
                LoanStatementRequestDto
                        .builder()
                        .amount(BigDecimal.valueOf(500_000))
                        .term(12)
                        .build();

        List<LoanOfferDto> result = loanOffersService.getOffers(loanStatementRequestDto);

        for (LoanOfferDto loanOfferDto : result) {
            if (loanOfferDto.getIsInsuranceEnabled()) {
                assertEquals(BigDecimal.valueOf(600_000), loanOfferDto.getTotalAmount());
            }
        }
    }
}
