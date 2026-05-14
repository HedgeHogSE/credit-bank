package ru.neoflex.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import io.micrometer.core.instrument.Counter;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.deal.controller.dto.EmailMessage;
import ru.neoflex.deal.controller.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.controller.dto.LoanOfferDto;
import ru.neoflex.deal.controller.dto.LoanStatementRequestDto;
import ru.neoflex.deal.mapper.ClientMapper;
import ru.neoflex.deal.mapper.ScoringMapper;
import ru.neoflex.deal.mapper.StatementMapper;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.service.ApplicationProcessService;
import ru.neoflex.deal.service.StatementService;
import ru.neoflex.deal.service.StatementStateChangeEventPublisher;
import ru.neoflex.deal.service.command.LoanStatementRequestCommand;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DealController.class)
class DealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StatementService statementService;

    @MockitoBean
    private ApplicationProcessService applicationProcessService;

    @MockitoBean
    private ClientMapper clientMapper;

    @MockitoBean
    private StatementMapper statementMapper;

    @MockitoBean
    private ScoringMapper scoringMapper;

    @MockitoBean
    private MeterRegistry meterRegistry;

    @MockitoBean
    private StatementStateChangeEventPublisher publisher;

    @MockitoBean
    private Counter counter;

    @BeforeEach
    void setUp() {

        when(meterRegistry.counter(anyString(), any(Iterable.class))).thenReturn(counter);
    }

    @Test
    void getLoanOffersShouldReturnOffers() throws Exception {
        LoanStatementRequestDto request = new LoanStatementRequestDto();
        List<LoanOfferDto> expectedOffers = List.of(
                LoanOfferDto.builder().build(),
                LoanOfferDto.builder().build()
        );

        when(clientMapper.toClientEntity(any())).thenReturn(new ClientEntity());
        when(statementMapper.toLoanStatementRequestCommand(any())).thenReturn(new LoanStatementRequestCommand());
        when(applicationProcessService.registerNewClientAndStatement(any(), any()))
                .thenReturn(expectedOffers);

        mockMvc.perform(post("/deal/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(counter).increment();
    }

    @Test
    void selectOfferShouldInvokeServiceAndPublisher() throws Exception {
        LoanOfferDto request = LoanOfferDto.builder().build();
        request.setStatementId(UUID.randomUUID());

        StatementEntity statementEntity = new StatementEntity();
        statementEntity.setStatementId(request.getStatementId());
        ClientEntity client = new ClientEntity();
        client.setEmail("test@test.com");
        statementEntity.setClientEntity(client);

        when(statementService.updateStatement(any())).thenReturn(statementEntity);

        mockMvc.perform(post("/deal/offer/select")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(publisher).publish(eq("finish-registration"), any(EmailMessage.class));
    }

    @Test
    void createCreditShouldReturnStatusCreated() throws Exception {
        UUID statementId = UUID.randomUUID();
        FinishRegistrationRequestDto request = FinishRegistrationRequestDto.builder().build();

        mockMvc.perform(post("/deal/calculate/{statementId}", statementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(applicationProcessService).completeRegistration(any(), eq(statementId));
    }
}
