package ru.neoflex.calculator.validation.validator;


import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.calculator.generator.DateNowGenerator;
import ru.neoflex.calculator.validation.annotation.Adult;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdultValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private Adult adultAnnotation;

    @Mock
    private DateNowGenerator dateNowGenerator;

    @InjectMocks
    AdultValidator adultValidator;

    @BeforeEach
    void setUp() {
        when(adultAnnotation.value()).thenReturn(18);
        adultValidator.initialize(adultAnnotation);
    }

    @ParameterizedTest()
    @CsvSource({
            "2000-02-02, true",
            "2006-03-28, true",
            "2015-01-01, false",
            "2023-12-12, false"
    })
    public void isValidShouldCheckAge(LocalDate date, boolean expected) {
        when(dateNowGenerator.generate()).thenReturn(LocalDate.parse("2026-03-13"));

        assertEquals(expected, adultValidator.isValid(date, context));
    }
}
