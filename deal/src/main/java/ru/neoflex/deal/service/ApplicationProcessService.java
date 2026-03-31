package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.controller.dto.LoanStatementRequestDto;
import ru.neoflex.deal.mapper.ClientMapper;
import ru.neoflex.deal.mapper.ScoringMapper;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.service.command.CreditCommand;
import ru.neoflex.deal.service.command.FinishRegistrationRequestCommand;
import ru.neoflex.deal.service.command.ScoringDataCommand;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationProcessService {

    private final ClientService clientService;
    private final StatementService statementService;
    private final ScoringService scoringService;
    private final CreditService creditService;

    private final ScoringMapper scoringMapper;
    private final ClientMapper clientMapper;

    @Transactional
    public StatementEntity registerNewClientAndStatement(ClientEntity clientEntity) {

        ClientEntity newClient = clientService.createClient(clientEntity);

        return statementService.createStatement(newClient);
    }

    @Transactional
    public void completeRegistration(FinishRegistrationRequestCommand request, UUID statementId) {
        StatementEntity statementEntity = statementService.getStatementByStatementId(statementId);

        ScoringDataCommand scoringData = scoringMapper.toScoringData(request, statementEntity);

        CreditCommand creditCommand = scoringService.getCredit(scoringData);

        creditService.createCredit(creditCommand);
    }
}
