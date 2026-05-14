package ru.neoflex.statement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;
import ru.neoflex.statement.controller.dto.LoanOfferDto;
import ru.neoflex.statement.controller.dto.LoanStatementRequestDto;
import ru.neoflex.statement.converter.StatementConverter;
import ru.neoflex.statement.exception.GlobalExceptionHandler;
import ru.neoflex.statement.service.OfferService;
import ru.neoflex.statement.service.command.LoanOfferCommand;
import ru.neoflex.statement.service.command.LoanStatementRequestCommand;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatementController.class)
@Import({GlobalExceptionHandler.class, StatementConverter.class})
@ExtendWith(SpringExtension.class)
public class StatementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private Clock clock;

    @MockitoBean
    private OfferService offerService;


    @Test
    void getLoanOffersReturnsListLoanOffersDtoWhenValid () throws Exception {

        LocalDate fixedDate = LocalDate.parse("2026-04-10");
        Instant instant = fixedDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        LoanStatementRequestDto request = createValidLoanStatementRequestDto();

        List<LoanOfferDto> response = createLoanOfferDtoList();

        when(offerService.getLoanOffers(any(LoanStatementRequestCommand.class))).thenReturn(response);

        mockMvc.perform(post("/statement")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    void getLoanOffersShouldThrowExceptionBecauseOfEmail() throws Exception {

        LocalDate fixedDate = LocalDate.parse("2026-04-10");
        Instant instant = fixedDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        LoanStatementRequestDto request = createValidLoanStatementRequestDto();
        request.setEmail("invalidemailsobakatochkacom");

        mockMvc.perform(post("/statement")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages")
                        .value(hasItem("Invalid email format")));
    }

    @Test
    void getLoanOffersShouldThrowExceptionBecauseOfAge() throws Exception {

        LocalDate fixedDate = LocalDate.parse("2026-04-10");
        Instant instant = fixedDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

        when(clock.instant()).thenReturn(instant);
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());

        LoanStatementRequestDto request = createValidLoanStatementRequestDto();
        request.setBirthDate(LocalDate.now().minusYears(5));

        mockMvc.perform(post("/statement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages")
                        .value(hasItem("User must be at least 18 years old")));
    }

    @Test
    void selectOfferCorrectWorkWhenValid() throws Exception {

        LoanOfferDto request = LoanOfferDto
                .builder()
                .statementId(UUID.randomUUID())
                .build();

        mockMvc.perform(post("/statement/offer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void selectOfferShouldThrowRestClientResponseException() throws Exception {

        LoanOfferDto request = LoanOfferDto
                .builder()
                .statementId(UUID.randomUUID())
                .build();

        RestClientResponseException exception = HttpServerErrorException.create(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                org.springframework.http.HttpHeaders.EMPTY,
                "{\"error\": \"test error\"}".getBytes(),
                java.nio.charset.StandardCharsets.UTF_8
        );

        doThrow(exception).when(offerService).selectOffer(any(LoanOfferCommand.class));

        mockMvc.perform(post("/statement/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    private LoanStatementRequestDto createValidLoanStatementRequestDto() {

        return LoanStatementRequestDto
                .builder()
                .amount(BigDecimal.valueOf(30000))
                .term(12)
                .email("test@test.com")
                .firstName("John")
                .lastName("Doe")
                .middleName("Smithovich")
                .birthDate(LocalDate.now().minusYears(25))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
    }

    private List<LoanOfferDto> createLoanOfferDtoList() {
        List<LoanOfferDto> loanOfferDtoList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            loanOfferDtoList.add(
                    LoanOfferDto
                            .builder()
                            .totalAmount(BigDecimal.valueOf(30000))
                            .build()
            );
        }
        return loanOfferDtoList;
    }
}
