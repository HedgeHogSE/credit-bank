package ru.neoflex.calculator.generator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DateNowGenerator {
    public LocalDate generate() {
        return LocalDate.now();
    }
}
