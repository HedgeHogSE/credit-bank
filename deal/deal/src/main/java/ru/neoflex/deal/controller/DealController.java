package ru.neoflex.deal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import ru.neoflex.deal.dto.*;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.entity.Statement;
import ru.neoflex.deal.service.ClientService;
import ru.neoflex.deal.service.CreditService;
import ru.neoflex.deal.service.StatementService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final ClientService clientService;
    private final StatementService statementService;
    private final CreditService creditService;

    private final RestClient restClient;

    @PostMapping("/statement")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody @Valid LoanStatementRequestDto request) {

        Client newClient = clientService.createClient(request);

        Statement newStatement = statementService.createStatement(newClient);

        List<LoanOfferDto> offers = restClient
                .post()
                .uri("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        assert offers != null;
        // System.out.println(newStatement.getStatementId());
        offers.forEach(offer -> offer.setStatementId(newStatement.getStatementId()));

        return ResponseEntity.ok(offers);
    }

    @PostMapping("/offer/select")
    public ResponseEntity<Void> selectOffer(@RequestBody @Valid LoanOfferDto request) {

        statementService.updateStatement(request);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> createCredit(@RequestBody @Valid FinishRegistrationRequestDto request,
                                             @PathVariable String statementId) {

        Statement statement = statementService.getStatementByStatementId(UUID.fromString(statementId));

        Client client = statement.getClient();

        ScoringDataDto scoringDataDto = ScoringDataDto
                .builder()
                .amount(statement.getAppliedOffer().getTotalAmount())
                .term(statement.getAppliedOffer().getTerm())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(request.getGender())
                .birthdate(client.getBirthDate())
                .passportSeries(client.getPassport().getSeries())
                .passportNumber(client.getPassport().getNumber())
                .passportIssueDate(request.getPassportIssueDate())
                .passportIssueBranch(request.getPassportIssueBranch())
                .maritalStatus(request.getMaritalStatus())
                .dependentAmount(request.getDependentAmount())
                .employment(request.getEmployment())
                .accountNumber(request.getAccountNumber())
                .isInsuranceEnabled(statement.getAppliedOffer().getIsInsuranceEnabled())
                .isSalaryClient(statement.getAppliedOffer().getIsSalaryClient())
                .build();

        CreditDto creditDto = restClient
                .post()
                .uri("/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .body(scoringDataDto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        assert creditDto != null;

        creditService.createCredit(creditDto);

        return ResponseEntity.ok().build();
    }
}
