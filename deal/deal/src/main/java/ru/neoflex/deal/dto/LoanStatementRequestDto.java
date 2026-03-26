package ru.neoflex.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Заявка на кредитное предложение")
@Data
@Builder
public class LoanStatementRequestDto {

    @Schema(description = "Запрашиваемая сумма", example = "100000")
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "12")
    private Integer term;

    @Schema(description = "Имя", example = "Ivan")
    private String firstName;

    @Schema(description = "Фамилия", example = "Ivanov")
    private String lastName;

    @Schema(description = "Отчество (необязательно)", example = "Ivanovich")
    private String middleName;

    @Schema(description = "Email адрес", example = "ivan.ivanov@gmail.com")
    private String email;

    @Schema(description = "Дата рождения (должен быть совершеннолетним)", example = "1990-02-21")
    private LocalDate birthDate;

    @Schema(description = "Серия паспорта", example = "1234")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "123456")
    private String passportNumber;
}

