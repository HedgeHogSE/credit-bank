package ru.neoflex.calculator.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.calculator.dto.LoanOfferDto;
import ru.neoflex.calculator.dto.LoanStatementRequestDto;
import ru.neoflex.calculator.service.LoanOffersService;

import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    private final LoanOffersService loanOffersService;

    @Autowired
    public CalculatorController(LoanOffersService loanOffersService) {
        this.loanOffersService = loanOffersService;
    }

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        return ResponseEntity.ok(
                loanOffersService.createOffers(loanStatementRequestDto)
        );
    }
}
