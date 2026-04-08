package ru.neoflex.statement.converter;

import org.springframework.stereotype.Component;
import ru.neoflex.statement.controller.dto.LoanOfferDto;
import ru.neoflex.statement.controller.dto.LoanStatementRequestDto;
import ru.neoflex.statement.service.command.LoanOfferCommand;
import ru.neoflex.statement.service.command.LoanStatementRequestCommand;

@Component
public class StatementConverter {

    public LoanStatementRequestCommand convertLoanStatementRequestCommand(
            LoanStatementRequestDto loanStatementRequestDto) {

        return LoanStatementRequestCommand
                .builder()
                .amount(loanStatementRequestDto.getAmount())
                .email(loanStatementRequestDto.getEmail())
                .term(loanStatementRequestDto.getTerm())
                .birthDate(loanStatementRequestDto.getBirthDate())
                .firstName(loanStatementRequestDto.getFirstName())
                .lastName(loanStatementRequestDto.getLastName())
                .middleName(loanStatementRequestDto.getMiddleName())
                .passportNumber(loanStatementRequestDto.getPassportNumber())
                .passportSeries(loanStatementRequestDto.getPassportSeries())
                .build();
    }

    public LoanOfferCommand convertLoanOfferCommand(
            LoanOfferDto loanOfferDto) {

        return LoanOfferCommand
                .builder()
                .statementId(loanOfferDto.getStatementId())
                .rate(loanOfferDto.getRate())
                .isInsuranceEnabled(loanOfferDto.getIsInsuranceEnabled())
                .isSalaryClient(loanOfferDto.getIsSalaryClient())
                .monthlyPayment(loanOfferDto.getMonthlyPayment())
                .requestedAmount(loanOfferDto.getRequestedAmount())
                .term(loanOfferDto.getTerm())
                .totalAmount(loanOfferDto.getTotalAmount())
                .build();
    }

}
