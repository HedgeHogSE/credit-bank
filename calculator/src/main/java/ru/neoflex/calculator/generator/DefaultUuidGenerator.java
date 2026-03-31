package ru.neoflex.calculator.generator;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DefaultUuidGenerator {
    public UUID generate() {
        return UUID.randomUUID();
    }
}
