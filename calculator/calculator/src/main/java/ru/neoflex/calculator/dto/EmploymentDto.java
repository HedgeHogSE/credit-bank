package ru.neoflex.calculator.dto;

import lombok.Builder;
import lombok.Data;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.enums.Position;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDto {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
