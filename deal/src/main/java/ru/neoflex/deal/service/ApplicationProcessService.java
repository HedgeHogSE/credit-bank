package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.controller.dto.LoanOfferDto;
import ru.neoflex.deal.controller.dto.LoanStatementRequestDto;
import ru.neoflex.deal.mapper.ClientMapper;
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
public class ApplicationProcessService {

    private final ClientService clientService;
    private final StatementService statementService;
    private final ScoringService scoringService;
    private final CreditService creditService;
    private final OfferService offerService;

    private final ScoringMapper scoringMapper;

    @Transactional
    public List<LoanOfferDto> registerNewClientAndStatement(ClientEntity clientEntity, LoanStatementRequestCommand command) {

        ClientEntity newClient = clientService.createClient(clientEntity);

        StatementEntity newStatement = statementService.createStatement(newClient);;

        return offerService.getLoanOffers(command, newStatement.getStatementId());
    }

    @Transactional
    public void completeRegistration(FinishRegistrationRequestCommand request, UUID statementId) {

        StatementEntity statementEntity = statementService.getStatementByStatementId(statementId);

        ClientEntity clientEntity = statementEntity.getClientEntity();
        clientService.complementClient(clientEntity, request);

        ScoringDataCommand scoringData = scoringMapper.toScoringData(request, statementEntity);

        CreditCommand creditCommand = scoringService.getCredit(scoringData);

        CreditEntity creditEntity = creditService.createCredit(creditCommand);

        statementEntity.setCreditEntity(creditEntity);
    }
}
