package ru.neoflex.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.neoflex.calculator.dto.*;
import ru.neoflex.calculator.enums.EmploymentStatus;
import ru.neoflex.calculator.enums.Gender;
import ru.neoflex.calculator.enums.MaritalStatus;
import ru.neoflex.calculator.enums.Position;
import ru.neoflex.calculator.exception.GlobalExceptionHandler;
import ru.neoflex.calculator.exception.ScoringException;
import ru.neoflex.calculator.generator.DateNowGenerator;
import ru.neoflex.calculator.service.CreditService;
import ru.neoflex.calculator.service.LoanOffersService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalculatorController.class)
@Import(GlobalExceptionHandler.class)
@ExtendWith(SpringExtension.class)
public class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoanOffersService loanOffersService;

    @MockitoBean
    private CreditService creditService;

    @MockitoBean
    private DateNowGenerator dateNowGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getLoanOffersReturnsListLoanOffersDtoWhenValid() throws Exception {

        when(dateNowGenerator.generate()).thenReturn(LocalDate.now());

        LoanStatementRequestDto request = createValidLoanStatementRequestDto();

        List<LoanOfferDto> response = createLoanOfferDtoList();

        when(loanOffersService.getOffers(any(LoanStatementRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/calculator/offers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }

    @Test
    @Disabled
    void getLoanOffersShouldThrowException() throws Exception {

        when(dateNowGenerator.generate()).thenReturn(LocalDate.now());

        LoanStatementRequestDto request = createValidLoanStatementRequestDto();
        request.setEmail("invalidemailsobakatochkacom");

        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].message")
                        .value(hasItem("Invalid email format")));
    }

    @Test
    void getCreditReturnsCreditDtoWhenValid() throws Exception {

        ScoringDataDto request = createValidScoringDataDto();

        CreditDto creditDto = createCreditDto();

        when(creditService.getCredit(any(ScoringDataDto.class))).thenReturn(creditDto);

        mockMvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.term").value(12));
    }

    @Test
    void getCreditShouldThrowException() throws Exception {

        ScoringDataDto request = createValidScoringDataDto();

        CreditDto creditDto = createCreditDto();

        when(creditService.getCredit(any(ScoringDataDto.class))).thenThrow(new ScoringException("Scoring Exception"));

        mockMvc.perform(post("/calculator/calc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Scoring Exception"));
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

    private ScoringDataDto createValidScoringDataDto() {

        return ScoringDataDto.builder()
                .amount(new BigDecimal("100000"))
                .term(12)
                .birthdate(LocalDate.now().minusYears(40))
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.SINGLE) // -0.01
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .employment(EmploymentDto.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED) // -0.01
                        .position(Position.WORKER) // -0.01
                        .salary(new BigDecimal("50000"))
                        .workExperienceTotal(24)
                        .workExperienceCurrent(12)
                        .build())
                .build();
    }

    private CreditDto createCreditDto() {

        return CreditDto
                .builder()
                .term(12)
                .build();
    }

}
