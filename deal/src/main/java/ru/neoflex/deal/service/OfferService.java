package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.neoflex.deal.controller.dto.LoanOfferDto;
import ru.neoflex.deal.service.command.LoanStatementRequestCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {

    private final RestClient restClient;

    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestCommand statement, UUID statementId) {
        log.info("Requesting loan offers for statementId: {}", statementId);
        List<LoanOfferDto> list = restClient
                .post()
                .uri("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(statement)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        list.forEach(offer -> offer.setStatementId(statementId));

        return list;
    }
}
