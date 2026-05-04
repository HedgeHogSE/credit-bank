package ru.neoflex.deal.service.command;

import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.model.dictionary.Gender;
import ru.neoflex.deal.model.dictionary.MaritalStatus;
import ru.neoflex.deal.model.Employment;

import java.time.LocalDate;

@Data
@Builder
public class FinishRegistrationRequestCommand {

    private Gender gender;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    private LocalDate passportIssueDate;

    private String passportIssueBranch;

    private Employment employment;

    private String accountNumber;
}
