package ru.neoflex.deal.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.controller.dto.LoanOfferDto;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.LoanOffer;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.enums.ApplicationStatus;
import ru.neoflex.deal.enums.ChangeType;
import ru.neoflex.deal.model.StatusHistory;
import ru.neoflex.deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;

    public StatementEntity createStatement(ClientEntity client) {

        ApplicationStatus newStatus = ApplicationStatus.PREAPPROVAL;

        StatementEntity statementEntity = StatementEntity
                .builder()
                .clientEntity(client)
                .status(newStatus)
                .build();

        addStatus(statementEntity, newStatus);

        return statementRepository.save(statementEntity);
    }

    public StatementEntity createStatementByStatement(StatementEntity statementEntity) {

        ApplicationStatus newStatus = ApplicationStatus.PREAPPROVAL;

        statementEntity.setStatus(newStatus);

        addStatus(statementEntity, newStatus);

        return statementRepository.save(statementEntity);
    }

    public StatementEntity updateStatement(LoanOffer loanOffer) {

        StatementEntity statementEntity = getStatementByStatementId(loanOffer.getStatementId());

        statementEntity.setAppliedOffer(loanOffer);

        ApplicationStatus newStatus = ApplicationStatus.APPROVED;

        statementEntity.setStatus(newStatus);

        addStatus(statementEntity, newStatus);

        return statementRepository.save(statementEntity);
    }

    public StatementEntity getStatementByStatementId(UUID statementId) {

        return statementRepository.getStatementByStatementId(statementId)
                .orElseThrow(()-> new EntityNotFoundException("Statement with id " + statementId + " not found"));
    }

    private void addStatus(StatementEntity statement, ApplicationStatus status) {

        StatusHistory historyRecord = StatusHistory.builder()
                .status(status)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();

        statement.getStatusHistory().add(historyRecord);
    }
}
