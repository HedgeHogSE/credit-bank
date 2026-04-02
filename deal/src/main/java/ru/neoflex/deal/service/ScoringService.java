package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.neoflex.deal.service.command.CreditCommand;
import ru.neoflex.deal.service.command.ScoringDataCommand;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScoringService {

    private final RestClient restClient;

    public CreditCommand getCredit(ScoringDataCommand scoringDataCommand) {
        log.info("Requesting credit scoring calculation");
        return restClient
                .post()
                .uri("/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .body(scoringDataCommand)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
