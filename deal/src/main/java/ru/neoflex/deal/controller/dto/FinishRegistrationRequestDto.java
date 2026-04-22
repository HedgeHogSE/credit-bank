package ru.neoflex.deal.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.model.dictionary.Gender;
import ru.neoflex.deal.model.dictionary.MaritalStatus;

import java.time.LocalDate;

@Data
@Builder
@Schema(description = "Запрос на завершение регистрации")
public class FinishRegistrationRequestDto {

    @Schema(description = "Пол", example = "MALE")
    private Gender gender;

    @Schema(description = "Семейное положение", example = "MARRIED")
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "1")
    private Integer dependentAmount;

    @Schema(description = "Дата выдачи паспорта", example = "2015-01-15")
    private LocalDate passportIssueDate;

    @Schema(description = "Кем выдан паспорт", example = "Отделение УФМС РФ")
    private String passportIssueBranch;

    @Schema(description = "Данные о занятости")
    private EmploymentDto employment;

    @Schema(description = "Номер счета", example = "11223344556677889900")
    private String accountNumber;

}
