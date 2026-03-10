package ru.neoflex.calculator.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import ru.neoflex.calculator.validation.annotation.Adult;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanStatementRequestDto {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "20000.0", message = "Amount must be greater than or equal to 20000")
    private BigDecimal amount;

    @NotNull(message = "Term is required")
    @Min(value = 6, message = "Term must be greater than or equal to 6")
    private Integer term;

    @NotNull(message = "First name is required")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "First name must consist of 2-30 Latin letters")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Last name must consist of 2-30 Latin letters")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Middle name must consist of 2-30 Latin letters")
    private String middleName;

    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$")
    private String email;

    @NotNull(message = "Birthdate is required")
    @Adult
    private LocalDate birthDate;

    @NotNull(message = "Passport series is required")
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must be 4 digits")
    private String passportSeries;

    @NotNull(message = "Passport number is required")
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must be 6 digits")
    private String passportNumber;
}
