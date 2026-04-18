package ru.neoflex.statement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.statement.controller.dto.LoanOfferDto;
import ru.neoflex.statement.controller.dto.LoanStatementRequestDto;
import ru.neoflex.statement.converter.StatementConverter;
import ru.neoflex.statement.service.OfferService;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Statement Controller", description = "API для работы с кредитными предложениями и заявками")
public class StatementController {

    private final OfferService statementService;
    private final StatementConverter statementConverter;

    @Operation(summary = "Получение предложений по кредиту", description = "Рассчитывает возможные условия кредита и возвращает список предложений")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный расчет предложений"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    })
    @PostMapping
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(
            @RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {

        log.info("Received request to calculate loan offers: {}", loanStatementRequestDto);

        List<LoanOfferDto> list = statementService.getLoanOffers(
                statementConverter.convertLoanStatementRequestCommand(loanStatementRequestDto)
        );

        log.info("Returning {} loan offers for request amount: {}", list.size(), loanStatementRequestDto.getAmount());
        return ResponseEntity.ok(list);

    }

    @Operation(summary = "Выбор предложения", description = "Выбирает одно из предложений по кредиту")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Предложение успешно выбрано"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    })
    @PostMapping("/offer")
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {

        log.info("Received request to select offer: {}", loanOfferDto);

        statementService.selectOffer(
                statementConverter.convertLoanOfferCommand(loanOfferDto)
        );

        log.info("Offer selected successfully for applicationId: {}", loanOfferDto.getStatementId());
    }
}
