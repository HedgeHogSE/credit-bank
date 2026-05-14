package ru.neoflex.statement.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.statement.exception.ExternalServiceException;
import ru.neoflex.statement.service.command.LoanStatementRequestCommand;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OfferServiceIntegrationTest {

    private static final WireMockServer wireMockServer = new WireMockServer(8081);

    @BeforeAll
    static void startWireMock() {

        wireMockServer.start();
        WireMock.configureFor("localhost", 8081);
    }

    @AfterAll
    static void stopWireMock() {

        wireMockServer.stop();
    }

    @Autowired
    private OfferService offerService;

    @Autowired
    private CircuitBreakerRegistry registry;

    @Test
    void shouldOpenCircuitBreakerAfterRepeatedFailures() {

        String cbName = "getLoanOffers";
        CircuitBreaker circuitBreaker = registry.circuitBreaker(cbName);

        circuitBreaker.reset();

        stubFor(post(urlEqualTo("/deal/statement"))
                .willReturn(serverError()));

        LoanStatementRequestCommand command = LoanStatementRequestCommand.builder().build();


        for (int i = 0; i < 5; i++) {
            assertThrows(ExternalServiceException.class, () -> offerService.getLoanOffers(command));
        }

        assertEquals(CircuitBreaker.State.OPEN, circuitBreaker.getState(),
                "Circuit Breaker должен быть в состоянии OPEN");

        assertThrows(ExternalServiceException.class, () -> offerService.getLoanOffers(command));

        verify(5, postRequestedFor(urlEqualTo("/deal/statement")));

    }

}