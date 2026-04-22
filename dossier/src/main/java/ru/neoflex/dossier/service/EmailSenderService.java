package ru.neoflex.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.neoflex.dossier.dto.EmailMessage;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailSenderService {

    public final JavaMailSender emailSender;

    public void sendEmail(EmailMessage message){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(message.getAddress());
        simpleMailMessage.setSubject(message.getTheme().toString());
        simpleMailMessage.setText(message.getText());

        log.info("Sending email: to={}, statementId={}, message={}",
                message.getAddress(), message.getStatementId(), message.getText());
        emailSender.send(simpleMailMessage);
        log.info("Email sent: to={}, statementId={}, message={}",
                message.getAddress(), message.getStatementId(), message.getText());
    }
}
