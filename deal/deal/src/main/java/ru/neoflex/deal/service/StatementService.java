package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.LoanOfferDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.enums.ApplicationStatus;
import ru.neoflex.deal.enums.ChangeType;
import ru.neoflex.deal.json.StatusHistory;
import ru.neoflex.deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;

    public Statement createStatement(Client client) {

        ApplicationStatus newStatus = ApplicationStatus.PREAPPROVAL;

        Statement statement = Statement
                .builder()
                .client(client)
                .status(newStatus) // ???
                .statusHistory(new ArrayList<>())
                .build();

        addStatus(statement, newStatus); // ???

        return statementRepository.save(statement);
    }

    public Statement updateStatement(LoanOfferDto loanOfferDto) {

        Statement statement = getStatementByStatementId(loanOfferDto.getStatementId());

        statement.setAppliedOffer(loanOfferDto);

        ApplicationStatus newStatus = ApplicationStatus.APPROVED;

        statement.setStatus(newStatus);

        addStatus(statement, newStatus);

        return statementRepository.save(statement);
    }

    public Statement getStatementByStatementId(UUID statementId) {

        return statementRepository.getStatementByStatementId(statementId);
    }

    private void addStatus(Statement statement, ApplicationStatus status) {
        if (statement.getStatusHistory() == null) {
            statement.setStatusHistory(new ArrayList<>());
        }

        StatusHistory historyRecord = StatusHistory.builder()
                .status(status)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();

        statement.getStatusHistory().add(historyRecord);
    }
}
