package ru.neoflex.statement.service.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import ru.neoflex.statement.validation.annotation.Adult;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public class LoanStatementRequestCommand {

    private BigDecimal amount;

    private Integer term;

    private String firstName;

    private String lastName;

    private String middleName;

    private String email;

    private LocalDate birthDate;

    private String passportSeries;

    private String passportNumber;
}
