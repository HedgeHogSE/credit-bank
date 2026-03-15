package ru.neoflex.calculator.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.calculator.dto.CreditDto;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.dto.ScoringDataDto;
import ru.neoflex.calculator.service.CreditService;
import ru.neoflex.calculator.service.LoanOffersService;

import java.util.List;

@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
public class CalculatorController {

    private static final Logger log =
            LoggerFactory.getLogger(CalculatorController.class);

    private final LoanOffersService loanOffersService;
    private final CreditService creditService;

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {

        log.info("Request: POST /calculator/offers");

        return ResponseEntity.ok(
                loanOffersService.getOffers(loanStatementRequestDto)
        );
    }

    @PostMapping("/calc")
    public ResponseEntity<CreditDto> getCredit(@RequestBody @Valid ScoringDataDto scoringDataDto) {

        log.info("Request: POST /calculator/calc");

        return ResponseEntity.ok(
                creditService.getCredit(scoringDataDto)
        );
    }
}
