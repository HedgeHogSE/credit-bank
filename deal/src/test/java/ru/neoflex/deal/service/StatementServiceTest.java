package ru.neoflex.deal.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.model.dictionary.ApplicationStatus;
import ru.neoflex.deal.model.dictionary.ChangeType;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.LoanOffer;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.model.StatusHistory;
import ru.neoflex.deal.repository.StatementRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatementServiceTest {

    @Mock
    private StatementRepository statementRepository;

    @Captor
    private ArgumentCaptor<StatementEntity> statementCaptor;

    @InjectMocks
    private StatementService statementService;

    @Test
    void createStatementShouldReturnStatementEntityAndCallRepositorySaveAndAddStatusPreapproval() {

        ClientEntity clientEntity = new ClientEntity();

        statementService.createStatement(clientEntity);

        verify(statementRepository, times(1)).save(statementCaptor.capture());

        StatementEntity statementEntity = statementCaptor.getValue();

        assertEquals(ApplicationStatus.PREAPPROVAL, statementEntity.getStatus());

        StatusHistory history = statementEntity.getStatusHistory().getFirst();
        assertEquals(ChangeType.AUTOMATIC, history.getChangeType());

    }

    @Test
    void updateStatementShouldUpdateApplicationStatusApprovedAndAddAppliedOffer() {

        LoanOffer loanOffer = LoanOffer
                .builder()
                .statementId(UUID.randomUUID())
                .term(12)
                .build();

        StatementEntity statementEntity = new StatementEntity();

        when(statementRepository.findByStatementId(loanOffer.getStatementId())).thenReturn(Optional.of(statementEntity));

        statementService.updateStatement(loanOffer);

        verify(statementRepository, times(1)).save(statementCaptor.capture());

        StatementEntity statementEntityBeforeUpdate = statementCaptor.getValue();

        assertEquals(ApplicationStatus.APPROVED, statementEntityBeforeUpdate.getStatus());
        assertEquals(loanOffer.getTerm(), statementEntityBeforeUpdate.getAppliedOffer().getTerm());

    }

    @Test
    void getStatementByStatementIdShouldReturnStatementEntity() {

        UUID statementId = UUID.randomUUID();
        StatementEntity statementEntity = StatementEntity.builder().statementId(statementId).build();

        when(statementRepository.findByStatementId(statementId)).thenReturn(Optional.of(statementEntity));

        StatementEntity result = statementService.getStatementByStatementId(statementId);

        assertEquals(statementId, result.getStatementId());

    }

    @Test
    void getStatementByStatementIdShouldThrowEntityNotFoundException() {

        UUID statementId = UUID.randomUUID();

        when(statementRepository.findByStatementId(statementId))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> statementService.getStatementByStatementId(statementId));
    }
}
