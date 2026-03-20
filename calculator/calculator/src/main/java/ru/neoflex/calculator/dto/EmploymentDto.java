package ru.neoflex.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.enums.Position;

import java.math.BigDecimal;

@Schema(description = "Данные о трудоустройстве и доходах")
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
    private Position position;

    @Schema(description = "Общий стаж работы (в месяцах)", example = "60")
    private Integer workExperienceTotal;

    @Schema(description = "Текущий стаж работы (в месяцах)", example = "24")
    private Integer workExperienceCurrent;
}
