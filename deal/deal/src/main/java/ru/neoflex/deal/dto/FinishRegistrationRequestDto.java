package ru.neoflex.deal.dto;

import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.enums.Gender;
import ru.neoflex.deal.enums.MaritalStatus;

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
