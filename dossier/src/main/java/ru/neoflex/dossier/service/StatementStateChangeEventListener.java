package ru.neoflex.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.dto.EmailMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementStateChangeEventListener {

    private final EmailSenderService emailService;

    @KafkaListener(
            topics = {
                    "finish-registration",
                    "create-documents",
                    "statement-denied",
                    "send-documents",
                    "send-ses",
                    "credit-issued"
            },
            groupId = "email-service-group"
    )
    public void handleEmailEvents(ConsumerRecord<String, EmailMessage> record) {

        EmailMessage message = record.value();

        log.info("Received event: topic={}, key={}, statementId={}, theme={}",
                record.topic(),
                record.key(),
                message.getStatementId(),
                message.getTheme()
        );

        switch (record.topic()) {
            case "finish-registration",
                 "create-documents",
                 "statement-denied",
                 "send-documents",
                 "send-ses",
                 "credit-issued" -> emailService.sendEmail(message);
            default -> log.warn("Unknown topic: {}", record.topic());
        }
    }

}
