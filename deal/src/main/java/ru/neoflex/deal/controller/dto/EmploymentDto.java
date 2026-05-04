package ru.neoflex.deal.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.model.dictionary.EmploymentPosition;
import ru.neoflex.deal.model.dictionary.EmploymentStatus;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDto {

    @Schema(description = "Статус трудоустройства", example = "EMPLOYED")
    private EmploymentStatus employmentStatus;

    @Schema(description = "ИНН работодателя", example = "7777777777")
    private String employerINN;

    @Schema(description = "Заработная плата клиента", example = "90000")
    private BigDecimal salary;

    @Schema(description = "Должность", example = "WORKER")
    private EmploymentPosition position;

    @Schema(description = "Общий стаж работы (в месяцах)", example = "60")
    private Integer workExperienceTotal;

    @Schema(description = "Текущий стаж работы (в месяцах)", example = "24")
    private Integer workExperienceCurrent;
}