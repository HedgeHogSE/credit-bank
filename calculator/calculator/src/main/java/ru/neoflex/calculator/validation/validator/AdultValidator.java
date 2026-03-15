package ru.neoflex.calculator.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.neoflex.calculator.generator.DateNowGenerator;
import ru.neoflex.calculator.validation.annotation.Adult;

import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    private int minAge;
    private final DateNowGenerator dateNowGenerator;

    @Override
    public void initialize(Adult constraintAnnotation) {
        this.minAge = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        int age = Period.between(birthDate, dateNowGenerator.generate()).getYears();

        return age >= minAge;
    }
}
