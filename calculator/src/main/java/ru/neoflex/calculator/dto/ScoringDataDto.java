package ru.neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.neoflex.calculator.enums.Gender;
import ru.neoflex.calculator.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Полные данные клиента для расчета скоринга")
@Data
@Builder
public class ScoringDataDto {

    @Schema(description = "Запрашиваемая сумма кредита", example = "500000")
    private BigDecimal amount;

    @Schema(description = "Срок кредита в месяцах", example = "12")
    private Integer term;

    @Schema(description = "Имя", example = "Иван")
    private String firstName;

    @Schema(description = "Фамилия", example = "Иванов")
    private String lastName;

    @Schema(description = "Отчество", example = "Иванович")
    private String middleName;

    @Schema(description = "Пол", example = "MALE")
    private Gender gender;

    @Schema(description = "Дата рождения", example = "1990-01-01")
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "1234")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "567890")
    private String passportNumber;

    @Schema(description = "Дата выдачи паспорта", example = "2010-02-15")
    private LocalDate passportIssueDate;

    @Schema(description = "Кем выдан паспорт", example = "ОВД гор. Москва")
    private String passportIssueBranch;

    @Schema(description = "Семейное положение", example = "SINGLE")
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "0")
    private Integer dependentAmount;

    @Schema(description = "Данные о трудоустройстве")
    private EmploymentDto employment;

    @Schema(description = "Номер счета", example = "40817810099910004312")
    private String accountNumber;

    @Schema(description = "Согласие на страховку", example = "true")
    private Boolean isInsuranceEnabled;

    @Schema(description = "Является ли клиент зарплатным", example = "false")
    private Boolean isSalaryClient;
}
