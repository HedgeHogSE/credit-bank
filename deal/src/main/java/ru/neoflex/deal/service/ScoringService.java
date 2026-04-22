package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.neoflex.deal.exception.ErrorResponse;
import ru.neoflex.deal.exception.ScoringRejectedException;
import ru.neoflex.deal.service.command.CreditCommand;
import ru.neoflex.deal.service.command.ScoringDataCommand;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

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
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    String rawBody = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    log.error("Raw error response: {}", rawBody);
                    throw new ScoringRejectedException("Error from calculator: " + rawBody);
                })
                .body(new ParameterizedTypeReference<>() {});
    }
}
