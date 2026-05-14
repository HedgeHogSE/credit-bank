package ru.neoflex.deal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.deal.controller.dto.EmailMessage;
import ru.neoflex.deal.exception.SesException;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.model.dictionary.ApplicationStatus;
import ru.neoflex.deal.repository.StatementRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private StatementService statementService;

    @Mock
    private StatementRepository statementRepository;

    @Mock
    private StatementStateChangeEventPublisher publisher;

    @InjectMocks
    private DocumentService documentService;

    private UUID statementId;
    private StatementEntity statement;
    private ClientEntity client;

    @BeforeEach
    void setUp() {
        statementId = UUID.randomUUID();
        client = new ClientEntity();
        client.setEmail("test@mail.com");

        statement = new StatementEntity();
        statement.setStatementId(statementId);
        statement.setClientEntity(client);
    }

    @Test
    void sendDocumentsShouldUpdateStatusAndPublishEvent() {

        when(statementService.getStatementByStatementId(statementId)).thenReturn(statement);

        documentService.sendDocuments(statementId);

        verify(statementService).addStatus(statement, ApplicationStatus.PREPARE_DOCUMENTS);

        verify(publisher).publish(eq("send-documents"), any(EmailMessage.class));

        verify(statementRepository).save(statement);
    }

    @Test
    void sendSesCodeShouldGenerateCodeAndPublishIt() {

        when(statementService.getStatementByStatementId(statementId)).thenReturn(statement);


        documentService.sendSesCode(statementId);


        assertNotNull(statement.getSesCode());
        assertTrue(statement.getSesCode().matches("\\d{4}"));
        verify(statementService).addStatus(statement, ApplicationStatus.DOCUMENTS_CREATED);
        verify(publisher).publish(eq("send-ses"), any(EmailMessage.class));
        verify(statementRepository).save(statement);
    }

    @Test
    void signDocumentsWhenCodeIsValidShouldCompleteSigning() {

        statement.setSesCode("1234");
        when(statementService.getStatementByStatementId(statementId)).thenReturn(statement);


        documentService.signDocuments(statementId, "1234");


        assertNull(statement.getSesCode());
        verify(statementService).addStatus(statement, ApplicationStatus.DOCUMENT_SIGNED);
        verify(publisher).publish(eq("credit-issued"), any(EmailMessage.class));
        verify(statementRepository).save(statement);
    }

    @Test
    void signDocumentsWhenCodeIsInvalidShouldThrowException() {

        statement.setSesCode("1234");
        when(statementService.getStatementByStatementId(statementId)).thenReturn(statement);


        assertThrows(SesException.class, () ->
                documentService.signDocuments(statementId, "wrong_code")
        );


        verify(statementRepository, never()).save(any());
        verify(publisher, never()).publish(any(), any());
    }
}