package com.example.greenpath.Controllers;

import com.example.greenpath.Config.CustomPasswordEncoder;
import com.example.greenpath.Models.Client;
import com.example.greenpath.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${jwt.secret}")
    private String base64Secret;

    @Autowired
    private ClientRepository clientRepository;

    private final CustomPasswordEncoder passwordEncoder = new CustomPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use.");
        }
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        clientRepository.save(client);
        return ResponseEntity.ok(Collections.singletonMap("message", "Registration successful!"));    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Client loginRequest) {
        Client client = clientRepository.findByEmail(loginRequest.getEmail());
        if (client == null || !passwordEncoder.matches(loginRequest.getPassword(), client.getPassword())) {
            return ResponseEntity.status(401).body("Invalid email or password.");
        }

        // Clé base64 depuis application.properties !
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Secret));

        String token = Jwts.builder()
                .setSubject(client.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24h
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return ResponseEntity.ok(token);
    }
}