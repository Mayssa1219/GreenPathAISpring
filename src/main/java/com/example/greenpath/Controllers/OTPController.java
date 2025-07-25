package com.example.greenpath.Controllers;

import com.example.greenpath.Models.Client;
import com.example.greenpath.Repositories.ClientRepository;
import com.example.greenpath.Services.MailService;
import com.example.greenpath.Models.OtpToken;
import com.example.greenpath.Repositories.OtpTokenRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class OTPController {

    @Autowired
    private MailService mailService;

    @Autowired
    private OtpTokenRepository otpTokenRepository;
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {
        String otp = generateOtp(6);

        // Sauvegarde OTP pour vérification ultérieure
        OtpToken otpToken = new OtpToken();
        otpToken.setEmail(email);
        otpToken.setCode(otp);
        otpToken.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpTokenRepository.save(otpToken);

        try {
            mailService.sendOtpEmail(email, otp);
            return ResponseEntity.ok(Collections.singletonMap("message", "OTP envoyé !"));
        } catch (MessagingException e) {
            return ResponseEntity
                    .status(500)
                    .body(Collections.singletonMap("message", "Erreur lors de l'envoi de l'OTP : " + e.getMessage()));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        Optional<OtpToken> otpTokenOpt = otpTokenRepository.findById(email);
        if (otpTokenOpt.isEmpty()) {
            return ResponseEntity
                    .status(400)
                    .body(Collections.singletonMap("message", "Aucun OTP trouvé pour cet email."));
        }
        OtpToken otpToken = otpTokenOpt.get();

        if (otpToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpTokenRepository.deleteById(email); // Nettoyage sécurité
            return ResponseEntity
                    .status(400)
                    .body(Collections.singletonMap("message", "OTP expiré."));
        }

        if (!otpToken.getCode().trim().equals(otp.trim())) {
            return ResponseEntity
                    .status(400)
                    .body(Collections.singletonMap("message", "OTP incorrect."));
        }

        // Activer le compte client
        Client client = clientRepository.findByEmail(email);
        if (client != null) {
            client.setStatut("actif");
            clientRepository.save(client);
        }

        otpTokenRepository.deleteById(email); // Supprime l'OTP dès qu'il a été utilisé

        // Envoi du mail de succès (gestion d'erreur)
        if (client != null) {
            try {
                // Tu peux personnaliser le message
                String message = "Bonjour " + client.getFullname() + ",<br>Votre compte a été activé avec succès !";
                mailService.sendSuccessEmail(client.getEmail(), message);
            } catch (Exception e) {
                // log.error("Erreur lors de l'envoi du mail de succès à " + email, e);
                return ResponseEntity.ok(Collections.singletonMap("message",
                        "OTP vérifié, compte activé ! (Attention : l'email de confirmation n'a pas pu être envoyé)"));
            }
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "OTP vérifié, compte activé !"));
    }
    private String generateOtp(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}