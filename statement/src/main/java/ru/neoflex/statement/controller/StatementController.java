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

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
public class StatementController {

    private final OfferService statementService;
    private final StatementConverter statementConverter;

    @PostMapping
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(
            @RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {

        List<LoanOfferDto> list = statementService.getLoanOffers(
                statementConverter.convertLoanStatementRequestCommand(loanStatementRequestDto)
        );

        return ResponseEntity.ok(list);

    }

    @PostMapping("/offer")
    public void selectOffer(LoanOfferDto loanOfferDto) {

        statementService.selectOffer(
                statementConverter.convertLoanOfferCommand(loanOfferDto)
        );

    }
}
