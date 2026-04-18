package ru.neoflex.statement.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import ru.neoflex.statement.controller.dto.LoanOfferDto;
import ru.neoflex.statement.service.command.LoanOfferCommand;
import ru.neoflex.statement.service.command.LoanStatementRequestCommand;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(OfferService.class)
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
    void getLoanOffersShouldReturnListLoanOfferDto() throws Exception {

        LoanStatementRequestCommand command = LoanStatementRequestCommand.builder().build();

        List<LoanOfferDto> response = List.of(
                LoanOfferDto.builder().build(),
                LoanOfferDto.builder().build(),
                LoanOfferDto.builder().build(),
                LoanOfferDto.builder().build()
        );

        server.expect(requestTo("/deal/statement"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(response), MediaType.APPLICATION_JSON));

        List<LoanOfferDto> result = offerService.getLoanOffers(command);

        assertEquals(result.size(), response.size());

        server.verify();
    }

    @Test
    void selectOfferShouldCorrectWork() throws Exception {

        LoanOfferCommand command = LoanOfferCommand.builder().build();

        server.expect(requestTo("/deal/offer/select"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK));

        offerService.selectOffer(command);

        server.verify();
    }

}
