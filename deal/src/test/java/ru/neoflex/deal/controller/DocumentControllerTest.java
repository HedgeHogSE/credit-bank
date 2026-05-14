package ru.neoflex.deal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import ru.neoflex.deal.controller.dto.SesCodeDto;
import ru.neoflex.deal.exception.GlobalExceptionHandler;
import ru.neoflex.deal.service.DocumentService;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DocumentController.class)
@Import(GlobalExceptionHandler.class)
@ExtendWith(SpringExtension.class)
public class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID statementId = UUID.randomUUID();

    @Test
    void sendDocumentsShouldWorkCurrently() throws Exception {

        mockMvc.perform(post("/deal/document/{statementId}/send", statementId))
                .andExpect(status().isOk());

        verify(documentService, times(1)).sendDocuments(statementId);
    }

    @Test
    void createDocumentsShouldWorkCurrently() throws Exception {

        mockMvc.perform(post("/deal/document/{statementId}/sign", statementId))
                .andExpect(status().isOk());

        verify(documentService, times(1)).sendSesCode(statementId);
    }

    @Test
    void signDocumentsShouldWorkCurrently() throws Exception {

        SesCodeDto dto = new SesCodeDto();
        dto.setSesCode("123456");

        mockMvc.perform(post("/deal/document/{statementId}/code", statementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(documentService, times(1)).signDocuments(statementId, "123456");
    }

}
