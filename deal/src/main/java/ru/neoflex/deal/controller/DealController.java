package ru.neoflex.deal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
import ru.neoflex.deal.service.command.ScoringDataCommand;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Сделка", description = "Управление заявками на кредит")
public class DealController {

    private final ClientService clientService;
    private final StatementService statementService;
    private final CreditService creditService;
    private final ScoringService scoringService;
    private final ApplicationProcessService applicationProcessService;

    private final ClientMapper clientMapper;
    private final StatementMapper statementMapper;
    private final ScoringMapper scoringMapper;

    private final OfferService offerService;

    @PostMapping("/statement")
    @Operation(summary = "Расчет возможных условий кредита", description = "Создание клиента и заявки, получение предложений")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody @Valid LoanStatementRequestDto request) {

        log.info("POST /deal/statement");
        LoanStatementRequestCommand command = statementMapper.toLoanStatementRequestCommand(request);

        StatementEntity newStatement = applicationProcessService.registerNewClientAndStatement(clientMapper.toClientEntity(request));

        List<LoanOfferDto> offers = offerService.getLoanOffers(command, newStatement.getStatementId());

        log.info("Statement created: statementId={}, offersCount={}", newStatement.getStatementId(), offers.size());
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/offer/select")
    @Operation(summary = "Выбор одного из предложений", description = "Выбор клиентом одного из предложений кредита")
    public void selectOffer(@RequestBody @Valid LoanOfferDto request) {

        log.info("POST /deal/offer/select statementId={}", request.getStatementId());
        LoanOffer loanOffer = statementMapper.toLoanOffer(request);

        statementService.updateStatement(loanOffer);

    }

    @PostMapping("/calculate/{statementId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Завершение регистрации", description = "Полный расчет парметров кредита")
    public void createCredit(@RequestBody @Valid FinishRegistrationRequestDto request,
                                             @PathVariable UUID statementId) {

        log.info("POST /deal/calculate statementId={}", statementId);

        applicationProcessService.completeRegistration(scoringMapper.toFinishRegistrationRequestCommand(request), statementId);

    }
}
