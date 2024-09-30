package br.com.fiap.epictaskx.mail;

import jakarta.mail.MessagingException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MailConsumer {

    private final EmailService emailService;

    public MailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "email-queue")
    public void sendMail(String message) throws MessagingException {
        emailService.sendEmail(message);
    }

}
