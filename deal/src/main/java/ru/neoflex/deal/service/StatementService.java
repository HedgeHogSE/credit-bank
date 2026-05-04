package ru.neoflex.deal.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.LoanOffer;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.model.dictionary.ApplicationStatus;
import ru.neoflex.deal.model.dictionary.ChangeType;
import ru.neoflex.deal.model.StatusHistory;
import ru.neoflex.deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatementService {

    private final StatementRepository statementRepository;

    public StatementEntity createStatement(ClientEntity client) {
        log.info("Creating statement for client: {}", client.getClientId());

        ApplicationStatus newStatus = ApplicationStatus.PREAPPROVAL;

        StatementEntity statementEntity = StatementEntity
                .builder()
                .clientEntity(client)
                .build();

        addStatus(statementEntity, newStatus);

        return statementRepository.save(statementEntity);
    }

    @Transactional
    public StatementEntity updateStatement(LoanOffer loanOffer) {
        log.info("Updating statement {} with accepted offer", loanOffer.getStatementId());

        StatementEntity statementEntity = getStatementByStatementId(loanOffer.getStatementId());

        if (statementEntity.getStatus() == ApplicationStatus.APPROVED) {
            log.info("Statement is already approved, skipping update");
            return statementEntity;
        }

        statementEntity.setAppliedOffer(loanOffer);

        ApplicationStatus newStatus = ApplicationStatus.APPROVED;

        addStatus(statementEntity, newStatus);

        return statementRepository.save(statementEntity);
    }

    @Transactional
    public StatementEntity getStatementByStatementId(UUID statementId) {

        return statementRepository.findByStatementId(statementId)
                .orElseThrow(()-> new EntityNotFoundException("Statement with id " + statementId + " not found"));
    }

    public void addStatus(StatementEntity statement, ApplicationStatus status) {

        statement.setStatus(status);

        StatusHistory historyRecord = StatusHistory.builder()
                .status(status)
                .time(LocalDateTime.now())
                .changeType(ChangeType.AUTOMATIC)
                .build();

        statement.getStatusHistory().add(historyRecord);
    }
}
