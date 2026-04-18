package ru.neoflex.deal.service;

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
import org.springframework.web.client.RestClientResponseException;
import ru.neoflex.deal.service.command.CreditCommand;
import ru.neoflex.deal.service.command.ScoringDataCommand;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ScoringService.class)
public class ScoringServiceTest {

    @Autowired
    private ScoringService scoringService;

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
    void getCreditShouldReturnCreditCommand() throws Exception {

        ScoringDataCommand scoringDataCommand = new ScoringDataCommand();

        CreditCommand creditCommand = CreditCommand.builder()
                .amount(BigDecimal.valueOf(100000))
                .build();

        server.expect(requestTo("/calculator/calc"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(creditCommand), MediaType.APPLICATION_JSON));

        CreditCommand result = scoringService.getCredit(scoringDataCommand);

        assertEquals(creditCommand.getAmount(), result.getAmount());

        server.verify();
    }

    @Test
    void getCreditShouldThrowRestClientResponseException() {

        ScoringDataCommand scoringDataCommand = new ScoringDataCommand();

        server.expect(requestTo("/calculator/calc"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(RestClientResponseException.class, () -> scoringService.getCredit(scoringDataCommand));

        server.verify();
    }

}
