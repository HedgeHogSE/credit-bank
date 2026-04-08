package ru.neoflex.statement.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import ru.neoflex.statement.validation.annotation.Adult;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    private int minAge;
    private final Clock clock;

    @Override
    public void initialize(Adult constraintAnnotation) {
        this.minAge = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {

        LocalDate now = LocalDate.now(clock);
        int age = Period.between(birthDate, now).getYears();

        return age >= minAge;
    }
}
