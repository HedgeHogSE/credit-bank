package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.controller.dto.EmailMessage;
import ru.neoflex.deal.controller.dto.LoanOfferDto;
import ru.neoflex.deal.model.dictionary.ApplicationStatus;
import ru.neoflex.deal.model.Theme;
import ru.neoflex.deal.exception.ScoringRejectedException;
import ru.neoflex.deal.mapper.ScoringMapper;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.CreditEntity;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.service.command.CreditCommand;
import ru.neoflex.deal.service.command.FinishRegistrationRequestCommand;
import ru.neoflex.deal.service.command.LoanStatementRequestCommand;
import ru.neoflex.deal.service.command.ScoringDataCommand;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationProcessService {

    private final ClientService clientService;
    private final StatementService statementService;
    private final ScoringService scoringService;
    private final CreditService creditService;
    private final OfferService offerService;

    private final StatementStateChangeEventPublisher publisher;

    private final ScoringMapper scoringMapper;

    @Transactional
    public List<LoanOfferDto> registerNewClientAndStatement(ClientEntity clientEntity, LoanStatementRequestCommand command) {

        ClientEntity newClient = clientService.createClient(clientEntity);

        StatementEntity newStatement = statementService.createStatement(newClient);

        return offerService.getLoanOffers(command, newStatement.getStatementId());
    }

    @Transactional
    public void completeRegistration(FinishRegistrationRequestCommand request, UUID statementId) {

        StatementEntity statementEntity = statementService.getStatementByStatementId(statementId);

        ClientEntity clientEntity = statementEntity.getClientEntity();
        clientService.complementClient(clientEntity, request);

        ScoringDataCommand scoringData = scoringMapper.toScoringData(request, statementEntity);

        try {
            CreditCommand creditCommand = scoringService.getCredit(scoringData);
            CreditEntity creditEntity = creditService.createCredit(creditCommand);

            ApplicationStatus newStatus = ApplicationStatus.CC_APPROVED;
            statementService.addStatus(statementEntity, newStatus);
            statementEntity.setCreditEntity(creditEntity);

            publisher.publish("create-documents",
                    EmailMessage
                            .builder()
                            .address(clientEntity.getEmail())
                            .theme(Theme.CREATE_DOCUMENTS)
                            .statementId(statementId)
                            .text("Черновик заявки на кредит успешно создан")
                            .build()
            );

        } catch (ScoringRejectedException ex) {
            log.info("Applying rejection logic for statement {}", statementId);
            ApplicationStatus newStatus = ApplicationStatus.CC_DENIED;
            statementService.addStatus(statementEntity, newStatus);

            publisher.publish("statement-denied",
                    EmailMessage
                            .builder()
                            .address(clientEntity.getEmail())
                            .theme(Theme.STATEMENT_DENIED)
                            .statementId(statementId)
                            .text("Заявка на оформление кредита отклонена")
                            .build()
            );
        }


    }
}
