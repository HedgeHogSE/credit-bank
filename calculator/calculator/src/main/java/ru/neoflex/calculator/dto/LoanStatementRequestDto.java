package ru.neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import ru.neoflex.calculator.validation.annotation.Adult;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Заявка на кредитное предложение")
@Data
@Builder
public class LoanStatementRequestDto {

    @Schema(description = "Запрашиваемая сумма", example = "100000")
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "20000.0", message = "Amount must be greater than or equal to 20000")
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "12")
    @NotNull(message = "Term is required")
    @Min(value = 6, message = "Term must be greater than or equal to 6")
    private Integer term;

    @Schema(description = "Имя", example = "Ivan")
    @NotNull(message = "First name is required")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "First name must consist of 2-30 Latin letters")
    private String firstName;

    @Schema(description = "Фамилия", example = "Ivanov")
    @NotNull(message = "Last name is required")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Last name must consist of 2-30 Latin letters")
    private String lastName;

    @Schema(description = "Отчество (необязательно)", example = "Ivanovich")
    @Pattern(regexp = "^[a-zA-Z]{2,30}$", message = "Middle name must consist of 2-30 Latin letters")
    private String middleName;

    @Schema(description = "Email адрес", example = "ivan.ivanov@gmail.com")
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$")
    private String email;

    @Schema(description = "Дата рождения (должен быть совершеннолетним)", example = "1990-02-21")
    @NotNull(message = "Birthdate is required")
    @Adult
    private LocalDate birthDate;

    @Schema(description = "Серия паспорта", example = "1234")
    @NotNull(message = "Passport series is required")
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must be 4 digits")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "123456")
    @NotNull(message = "Passport number is required")
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must be 6 digits")
    private String passportNumber;
}
