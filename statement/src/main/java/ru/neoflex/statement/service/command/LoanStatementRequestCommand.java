package ru.neoflex.statement.service.command;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
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
