package ru.neoflex.calculator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.service.CreditService;
import ru.neoflex.calculator.service.LoanOffersService;

import java.util.List;

@Tag(name = "Калькулятор", description = "Расчет кредитных операций")
@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
@Slf4j
public class CalculatorController {

    private final LoanOffersService loanOffersService;
    private final CreditService creditService;

    @Operation(summary = "Расчёт возможных условий кредита (прескоринг)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный расчет предложений"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные запроса")
    })
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(
            @RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {

        log.info("Request: POST /calculator/offers, data: {}", loanStatementRequestDto);
        List<LoanOfferDto> offers = loanOffersService.getOffers(loanStatementRequestDto);
        log.info("Response: POST /calculator/offers, count: {}", offers.size());

        return ResponseEntity.ok(offers);
    }

    @Operation(summary = "Полный расчёт параметров кредита (скоринг)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный расчет параметров кредита"),
            @ApiResponse(responseCode = "400", description = "Отказ по скорингу или невалидные данные")
    })
    @PostMapping("/calc")
    public ResponseEntity<CreditDto> getCredit(@RequestBody @Valid ScoringDataDto scoringDataDto) {

        log.info("Request: POST /calculator/calc, data: {}", scoringDataDto);
        CreditDto credit = creditService.getCredit(scoringDataDto);
        log.info("Response: POST /calculator/calc, credit amount: {}, psk: {}", credit.getAmount(), credit.getPsk());

        return ResponseEntity.ok(credit);
    }
}
