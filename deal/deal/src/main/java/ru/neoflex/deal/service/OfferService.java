package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.neoflex.deal.controller.dto.LoanOfferDto;
import ru.neoflex.deal.controller.dto.LoanStatementRequestDto;
import ru.neoflex.deal.mapper.StatementMapper;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.service.command.LoanStatementRequestCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferService {

    private final RestClient restClient;

    private final StatementMapper statementMapper;

    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestCommand statement, UUID statementId) {

        LoanStatementRequestDto request = statementMapper.toLoanStatementRequestDto(statement);

        List<LoanOfferDto> list = restClient
                .post()
                .uri("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        list = (list == null) ? new ArrayList<>() : list;

        list.forEach(offer -> offer.setStatementId(statementId));

        return list;
    }
}
