package ru.neoflex.statement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.neoflex.statement.controller.dto.LoanOfferDto;
import ru.neoflex.statement.service.command.LoanOfferCommand;
import ru.neoflex.statement.service.command.LoanStatementRequestCommand;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final RestClient restClient;

    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestCommand command) {

        return restClient
                .post()
                .uri("/deal/statement")
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public void selectOffer(LoanOfferCommand command) {

        restClient
                .post()
                .uri("/deal/offer/select")
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }


}
