package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.controller.dto.EmailMessage;

@Service
@RequiredArgsConstructor
public class StatementStateChangeEventPublisher {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public void publish(String topic, EmailMessage message) {
        kafkaTemplate.send(topic, message);
    }
}
