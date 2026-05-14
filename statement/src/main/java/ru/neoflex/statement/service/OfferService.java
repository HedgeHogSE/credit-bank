package ru.neoflex.statement.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.neoflex.statement.controller.dto.LoanOfferDto;
import ru.neoflex.statement.exception.ExternalServiceException;
import ru.neoflex.statement.service.command.LoanOfferCommand;
import ru.neoflex.statement.service.command.LoanStatementRequestCommand;

import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OfferService {

    private final RestClient restClient;

    @CircuitBreaker(name = "getLoanOffers", fallbackMethod = "fallbackGetLoanOffers")
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestCommand command) {

        log.info("Sending deal/statement request for amount {}, term {}", command.getAmount(), command.getTerm());
        List<LoanOfferDto> response = restClient
                .post()
                .uri("/deal/statement")
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        log.info("Received {} loan offers from deal module", response.size());
        return response;
    }

    @CircuitBreaker(name = "selectOffer", fallbackMethod = "fallbackSelectOffer")
    public void selectOffer(LoanOfferCommand command) {

        log.info("Sending deal/offer/select request for statementId: {}", command.getStatementId());
        
        restClient
                .post()
                .uri("/deal/offer/select")
                .contentType(MediaType.APPLICATION_JSON)
                .body(command)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
                
        log.info("Successfully selected offer for statementId: {}", command.getStatementId());
    }

    public List<LoanOfferDto> fallbackGetLoanOffers(LoanStatementRequestCommand command, Exception e) {

        log.error("Circuit Breaker triggered for getLoanOffers. Reason: {}", e.getMessage());

        throw new ExternalServiceException("К сожалению, сервис подбора предложений сейчас недоступен. Попробуйте позже.");
    }

    public void fallbackSelectOffer(LoanOfferCommand command, Exception e) {

        log.error("Circuit Breaker triggered for selectOffer for ID: {}. Error: {}",
                command.getStatementId(), e.getMessage());

        throw new ExternalServiceException("Не удалось выбрать предложение, так как модуль сделок не отвечает.");
    }

}
