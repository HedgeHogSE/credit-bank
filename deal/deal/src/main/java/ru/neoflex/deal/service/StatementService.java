package ru.neoflex.deal.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.LoanOffer;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.dictionary.ApplicationStatus;
import ru.neoflex.deal.dictionary.ChangeType;
import ru.neoflex.deal.model.StatusHistory;
import ru.neoflex.deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
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

    public void updateStatement(LoanOffer loanOffer) {

        StatementEntity statementEntity = getStatementByStatementId(loanOffer.getStatementId());

        statementEntity.setAppliedOffer(loanOffer);

        ApplicationStatus newStatus = ApplicationStatus.APPROVED;

        statementEntity.setStatus(newStatus);

        addStatus(statementEntity, newStatus);

        statementRepository.save(statementEntity);
    }

    @Transactional(readOnly = true)
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
