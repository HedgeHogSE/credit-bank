package ru.neoflex.deal.controller.dto;

import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.dictionary.Gender;
import ru.neoflex.deal.dictionary.MaritalStatus;

import java.time.LocalDate;

@Data
@Builder
public class FinishRegistrationRequestDto {

    private Gender gender;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private LocalDate passportIssueDate;

    private String passportIssueBranch;

    private EmploymentDto employment;

    private String accountNumber;

}
