package ru.neoflex.calculator.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.calculator.dto.PaymentScheduleElementDto;
import ru.neoflex.calculator.generator.DateNowGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreditUtilTest {

    @Mock
    private DateNowGenerator dateNowGenerator;

    @ParameterizedTest()
    @CsvSource({
            "100000, 0.12, 12, 8884.88",
            "1000000, 0.10, 360, 8775.72",
            "1000, 0.05, 1, 1004.17",
    })
    void shouldCalculateCorrectMonthlyPayment(
            BigDecimal amount,
            BigDecimal rate,
            Integer term,
            BigDecimal expected) {

        BigDecimal actual = CreditUtil.calculateMonthlyPayment(amount, rate, term);

        assertEquals(0, actual.compareTo(expected));
    }

    @Test
    void calculateMonthlyPaymentShouldThrowArithmeticException() {
        assertThrows(ArithmeticException.class, () -> CreditUtil.calculateMonthlyPayment(BigDecimal.valueOf(100000), BigDecimal.ZERO, 12));
    }

    @Test
    void scheduleShouldDecreaseRemainingDebtToZero() {

        when(dateNowGenerator.generate()).thenReturn(LocalDate.parse("2026-03-13"));

        BigDecimal amount = new BigDecimal("100000.00");
        BigDecimal monthlyPayment = new BigDecimal("8791.59");
        BigDecimal rate = new BigDecimal("0.10");
        int term = 12;

        List<PaymentScheduleElementDto> schedule =
                CreditUtil.getPaymentScheduleElements(amount, monthlyPayment, rate, term, dateNowGenerator.generate());

        assertEquals(term, schedule.size());

        assertEquals(BigDecimal.ZERO, schedule.getLast().getRemainingDebt());

    }

    @ParameterizedTest
    @CsvSource({
            "12, 100000, 8791.59, 0.05",
            "6, 50000, 8578.47, 0.03"
    })
    void getPskShouldReturnCorrectCoefficient(int term, BigDecimal amount, BigDecimal payment, BigDecimal expected) {
        BigDecimal actual = CreditUtil.getPsk(term, amount, payment);

        assertEquals(expected, actual);
    }
}
