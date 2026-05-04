package ru.neoflex.deal.model;

import lombok.Data;
import ru.neoflex.deal.model.dictionary.EmploymentPosition;
import ru.neoflex.deal.model.dictionary.EmploymentStatus;

import java.math.BigDecimal;

@Data
public class Employment {
    private EmploymentStatus status;
    private String employerInn;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
