package com.example.greenpath.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    private final String from = "contact@greenpathai.com";

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom(from);
        mailSender.send(message);
    }

    private void sendHtmlEmail(String to, String subject, String templateName, Context context) throws MessagingException {
        String htmlContent = templateEngine.process(templateName, context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom(from);

        mailSender.send(message);
    }

    public void sendOtpEmail(String to, String otp) throws MessagingException {
        Context context = new Context();
        context.setVariable("otp", otp);
        sendHtmlEmail(to, "Votre code de vérification GreenPath", "mails/OTPMail.html", context);
    }

    public void sendResetPasswordEmail(String to, String resetUrl) throws MessagingException {
        Context context = new Context();
        context.setVariable("resetUrl", resetUrl);
        sendHtmlEmail(to, "Réinitialisation de votre mot de passe GreenPathAI", "mails/ResetPassword.html", context);
    }

    public void sendSuccessEmail(String to, String message) throws MessagingException {
        Context context = new Context();
        context.setVariable("message", message);
        sendHtmlEmail(to, "Opération réussie - GreenPathAI", "mails/SuccessMail.html", context);
    }
}