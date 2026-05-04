package ru.neoflex.deal.controller;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import ru.neoflex.deal.controller.dto.*;
import ru.neoflex.deal.model.Theme;
import ru.neoflex.deal.mapper.ClientMapper;
import ru.neoflex.deal.mapper.ScoringMapper;
import ru.neoflex.deal.mapper.StatementMapper;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.service.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Сделка", description = "Управление заявками на кредит")
public class DealController {

    private final StatementService statementService;
    private final ApplicationProcessService applicationProcessService;

    private final ClientMapper clientMapper;
    private final StatementMapper statementMapper;
    private final ScoringMapper scoringMapper;

    private final MeterRegistry meterRegistry;

    private final StatementStateChangeEventPublisher publisher;

    @PostMapping("/statement")
    @Operation(summary = "Расчет возможных условий кредита", description = "Создание клиента и заявки, получение предложений")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody @Valid LoanStatementRequestDto request) {

        log.info("POST /deal/statement");

        meterRegistry.counter("deal_counter", List.of()).increment();

        List<LoanOfferDto> offers = applicationProcessService.registerNewClientAndStatement(
                clientMapper.toClientEntity(request),
                statementMapper.toLoanStatementRequestCommand(request)
        );

        log.info("Statement created: offersCount={}", offers.size());
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/offer/select")
    @Operation(summary = "Выбор одного из предложений", description = "Выбор клиентом одного из предложений кредита")
    public void selectOffer(@RequestBody @Valid LoanOfferDto request) {

        log.info("POST /deal/offer/select statementId={}", request.getStatementId());

        StatementEntity newStatementEntity = statementService.updateStatement(
                statementMapper.toLoanOffer(request)
        );

        publisher.publish("finish-registration",
                EmailMessage
                        .builder()
                        .address(newStatementEntity.getClientEntity().getEmail())
                        .theme(Theme.FINISH_REGISTRATION)
                        .statementId(newStatementEntity.getStatementId())
                        .text("Приветствуем, вы начали оформление кредита")
                        .build()
        );

    }

    @PostMapping("/calculate/{statementId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Завершение регистрации", description = "Полный расчет параметров кредита")
    public void createCredit(@RequestBody @Valid FinishRegistrationRequestDto request,
                                             @PathVariable UUID statementId) {

        log.info("POST /deal/calculate statementId={}", statementId);

        applicationProcessService.completeRegistration(
                scoringMapper.toFinishRegistrationRequestCommand(request), statementId
        );

    }
}
