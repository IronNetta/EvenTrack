package org.seba.eventrack.bll.services.mails.impls;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.seba.eventrack.api.models.mails.dtos.EmailsDTO;
import org.seba.eventrack.bll.services.mails.EmailService;
import org.seba.eventrack.dl.entities.Event;
import org.seba.eventrack.dl.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.io.File;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public String sendSimpleMail(EmailsDTO details) {
        try {

            SimpleMailMessage mailMessage = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());

            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }

        catch (Exception e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        }
    }

    public String sendMailWithAttachment(EmailsDTO details) {
        try {
            Context context = new Context();
            context.setVariables(Map.of(
                    "subject", details.getSubject(),
                    "msgBody", details.getMsgBody()
            ));

            String emailContent = templateEngine.process("emails/email-with-attachment", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(details.getRecipient());
            mimeMessageHelper.setSubject(details.getSubject());
            mimeMessageHelper.setText(emailContent, true);

            FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
            mimeMessageHelper.addAttachment(file.getFilename(), file);

            javaMailSender.send(mimeMessage);
            return "Mail sent successfully with attachment";
        } catch (MessagingException e) {
            return "Error while sending mail: " + e.getMessage();
        }
    }

    public String sendEventReminderEmail(User user, Event event) throws MessagingException {
        try {
        Context context = new Context();
        context.setVariables(Map.of(
                "userName", user.getUsername(),
                "eventLocation", event.getLocation(),
                "eventDate", event.getDate().toString(),
                "eventOrganizer", event.getOrganizer()
        ));

        String emailContent = templateEngine.process("emails/event-reminder", context);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(user.getEmail());
        helper.setSubject("Rappel de votre événement : " + event.getTitle());
        helper.setText(emailContent, true);

        javaMailSender.send(message);

        return "Email sent successfully";
    }
        catch (MessagingException e) {
            return "Error while sending email: " + e.getMessage();
        }
    }
}
