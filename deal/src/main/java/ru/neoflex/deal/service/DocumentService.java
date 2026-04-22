package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.controller.dto.EmailMessage;
import ru.neoflex.deal.model.dictionary.ApplicationStatus;
import ru.neoflex.deal.model.Theme;
import ru.neoflex.deal.exception.SesException;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.repository.StatementRepository;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final StatementService statementService;

    private final StatementRepository statementRepository;

    private final StatementStateChangeEventPublisher publisher;

    public void sendDocuments(UUID statementId) {

        StatementEntity statementEntity = statementService.getStatementByStatementId(statementId);

        ApplicationStatus status = ApplicationStatus.PREPARE_DOCUMENTS;

        statementService.addStatus(statementEntity, status);

        publisher.publish("send-documents",
                EmailMessage
                        .builder()
                        .address(statementEntity.getClientEntity().getEmail())
                        .theme(Theme.SEND_DOCUMENTS)
                        .statementId(statementId)
                        .text("Высылаем итоговые документы для ознакомления")
                        .build()
        );

        statementRepository.save(statementEntity);

    }

    public void sendSesCode(UUID statementId) {

        StatementEntity statementEntity = statementService.getStatementByStatementId(statementId);

        ApplicationStatus status = ApplicationStatus.DOCUMENTS_CREATED;

        statementService.addStatus(statementEntity, status);

        statementEntity.setSesCode(generateSesCode());

        publisher.publish("send-ses",
                EmailMessage
                        .builder()
                        .address(statementEntity.getClientEntity().getEmail())
                        .theme(Theme.SEND_SES)
                        .statementId(statementId)
                        .text(String.format("Ваш код подтверждения для оформления кредита: %s", statementEntity.getSesCode()))
                        .build()
        );

        statementRepository.save(statementEntity);

    }

    public void signDocuments(UUID statementId, String sesCode) {

        StatementEntity statementEntity = statementService.getStatementByStatementId(statementId);

        if (!statementEntity.getSesCode().equals(sesCode)) {
            throw new SesException("Invalid SES code");
        }

        ApplicationStatus status = ApplicationStatus.DOCUMENT_SIGNED;

        statementService.addStatus(statementEntity, status);

        publisher.publish("credit-issued",
                EmailMessage
                        .builder()
                        .address(statementEntity.getClientEntity().getEmail())
                        .theme(Theme.CREDIT_ISSUED)
                        .statementId(statementId)
                        .text("Поздравляем, кредит оформлен")
                        .build()
        );

        statementEntity.setSesCode(null);

        statementRepository.save(statementEntity);
    }

    private String generateSesCode() {

        int max = 9999;
        int min = 1000;

        Random random = new Random();

        return String.valueOf(random.nextInt(max - min + 1) + min);
    }
}
