package ru.neoflex.deal.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import ru.neoflex.deal.controller.dto.LoanOfferDto;
import ru.neoflex.deal.exception.GlobalExceptionHandler;
import ru.neoflex.deal.service.command.LoanStatementRequestCommand;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(OfferService.class)
@Import(GlobalExceptionHandler.class)
public class OfferServiceTest {

    @Autowired
    private OfferService offerService;

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RestClient restClient(RestClient.Builder builder) {
            return builder.build();
        }
    }

    @Test
    void getLoanOffersShouldReturnListOfLoanOffersWithStatementId() throws Exception {

        UUID statementId = UUID.randomUUID();
        LoanStatementRequestCommand command = new LoanStatementRequestCommand();

        List<LoanOfferDto> loanOfferDtos = List.of(
                LoanOfferDto.builder().rate(BigDecimal.ONE).build(),
                LoanOfferDto.builder().rate(BigDecimal.ONE).build(),
                LoanOfferDto.builder().rate(BigDecimal.ONE).build(),
                LoanOfferDto.builder().rate(BigDecimal.ONE).build()
        );

        server.expect(requestTo("/calculator/offers"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(loanOfferDtos), MediaType.APPLICATION_JSON));

        List<LoanOfferDto> result = offerService.getLoanOffers(command, statementId);

        assertEquals(4, result.size());
        assertEquals(statementId, result.getFirst().getStatementId());

        server.verify();
    }

    @Test
    void getLoanOffersShouldThrowRestClientResponseException() {

        LoanStatementRequestCommand command = new LoanStatementRequestCommand();

        server.expect(requestTo("/calculator/offers"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RestClientResponseException.class, () -> offerService.getLoanOffers(command, UUID.randomUUID()));

        server.verify();
    }

}
