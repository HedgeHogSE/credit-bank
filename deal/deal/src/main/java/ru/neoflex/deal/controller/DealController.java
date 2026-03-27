package ru.neoflex.deal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.neoflex.deal.controller.dto.*;
import ru.neoflex.deal.mapper.ClientMapper;
import ru.neoflex.deal.mapper.ScoringMapper;
import ru.neoflex.deal.mapper.StatementMapper;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.LoanOffer;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.service.*;
import ru.neoflex.deal.service.command.CreditCommand;
import ru.neoflex.deal.service.command.LoanStatementRequestCommand;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final ClientService clientService;
    private final StatementService statementService;
    private final CreditService creditService;
    private final ScoringService scoringService;

    private final ClientMapper clientMapper;
    private final StatementMapper statementMapper;
    private final ScoringMapper scoringMapper;

    private final OfferService offerService;

    @PostMapping("/statement")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody @Valid LoanStatementRequestDto request) {

        ClientEntity newClient = clientService.createClient(clientMapper.toClientEntity(request));

        LoanStatementRequestCommand command = statementMapper.toLoanStatementRequestCommand(request);

        StatementEntity newStatement = statementService.createStatement(newClient);

        List<LoanOfferDto> offers = offerService.getLoanOffers(command, newStatement.getStatementId());

        return ResponseEntity.ok(offers);
    }

    @PostMapping("/offer/select")
    public void selectOffer(@RequestBody @Valid LoanOfferDto request) {

        LoanOffer loanOffer = statementMapper.toLoanOffer(request);

        statementService.updateStatement(loanOffer);

    }

    @PostMapping("/calculate/{statementId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCredit(@RequestBody @Valid FinishRegistrationRequestDto request,
                                             @PathVariable String statementId) {

        StatementEntity statementEntity = statementService.getStatementByStatementId(UUID.fromString(statementId));

        ScoringDataDto scoringData = scoringMapper.toScoringData(request, statementEntity);

        CreditCommand creditCommand = scoringService.getCredit(scoringMapper.toScoringDataCommand(scoringData));

        creditService.createCredit(creditCommand);

    }
}
