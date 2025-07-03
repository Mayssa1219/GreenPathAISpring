package com.example.greenpath.Controllers;

import com.example.greenpath.Config.CustomPasswordEncoder;
import com.example.greenpath.Models.Client;
import com.example.greenpath.Models.PasswordResetToken;
import com.example.greenpath.Repositories.ClientRepository;
import com.example.greenpath.Repositories.PasswordResetTokenRepository;
import com.example.greenpath.Services.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
@RestController
@RequestMapping("/api/auth")
public class PasswordResetTokenController {
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private MailService mailService;
    @Autowired
    private ClientRepository clientRepository;

    private final CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();

    @PostMapping("/request-reset-password")
    public ResponseEntity<?> requestResetPassword(@RequestParam String email) {
        Client client = clientRepository.findByEmail(email);
        if (client == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Aucun compte associé à cet email."));
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        passwordResetTokenRepository.save(resetToken);

        String resetUrl = "http://localhost:4200/reset-password?token=" + token;
        try {
            mailService.sendResetPasswordEmail(email, resetUrl);
        } catch (MessagingException e) {
            // Gère l’erreur (par exemple, retourne une réponse 500)
            return ResponseEntity.status(500).body(Collections.singletonMap("message", "Erreur lors de l'envoi de l'email."));
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "Email de réinitialisation envoyé !"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Token invalide ou expiré."));
        }

        Client client = clientRepository.findByEmail(resetToken.getEmail());
        if (client == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Utilisateur introuvable."));
        }

        client.setPassword(passwordEncoder.encode(newPassword));
        clientRepository.save(client);
        passwordResetTokenRepository.delete(resetToken);

        return ResponseEntity.ok(Collections.singletonMap("message", "Mot de passe réinitialisé avec succès."));
    }
}
