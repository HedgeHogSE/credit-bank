package ru.neoflex.calculator.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilBigDecimalTest {

    static Stream<Arguments> provideBigDecimalValues() {
        return Stream.of(
                Arguments.of(new BigDecimal("0.1"), new BigDecimal("0.2"), false),
                Arguments.of(new BigDecimal("0.4"), new BigDecimal("0.3"), true),
                Arguments.of(new BigDecimal("100000"), new BigDecimal("50000"), true),
                Arguments.of(new BigDecimal("1.1"), new BigDecimal("1.1"), false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBigDecimalValues")
    void isGreaterThanShouldReturnCorrectBoolean (BigDecimal num1, BigDecimal num2, boolean expectedResult) {
        assertEquals(expectedResult, UtilBigDecimal.isGreaterThan(num1, num2));
    }
}
