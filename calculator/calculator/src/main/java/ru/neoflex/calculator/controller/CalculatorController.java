package ru.neoflex.calculator.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final LoanOffersService loanOffersService;
    private final CreditService creditService;

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        return ResponseEntity.ok(
                loanOffersService.createOffers(loanStatementRequestDto)
        );
    }

    @PostMapping("/calc")
    public ResponseEntity<CreditDto> getCredit(@RequestBody @Valid ScoringDataDto scoringDataDto) {
        return ResponseEntity.ok(
                creditService.getCredit(scoringDataDto)
        );
    }
}
