package com.example.greenpath.Controllers;

import com.example.greenpath.Config.CustomPasswordEncoder;
import com.example.greenpath.Models.Client;
import com.example.greenpath.Repositories.ClientRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.*;
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
    @Value("${google.client-id}")
    private String googleClientId;

    @Value("${google.client-secret}")
    private String googleClientSecret;

    @Value("${google.redirect-uri}")
    private String googleRedirectUri;

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
    // 1. Redirection vers Google OAuth2
    @GetMapping("/google/login")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        String googleClientId = "30208622387-ra4vd5rf7j17vb7mhitta7bnmpl3kbc2.apps.googleusercontent.com"; // remplace par ta vraie valeur
        String googleRedirectUri = "http://localhost:8081/api/auth/google/callback"; // remplace si besoin
        String scope = "openid email profile";
        String responseType = "code";
        String accessType = "offline";
        String prompt = "consent"; // facultatif, force la sélection de compte

        String url = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + googleClientId
                + "&redirect_uri=" + googleRedirectUri
                + "&response_type=" + responseType
                + "&scope=" + scope.replace(" ", "%20")
                + "&access_type=" + accessType
                + "&prompt=" + prompt;

        response.sendRedirect(url);
    }

    // 2. Callback de Google
    @GetMapping("/google/callback")
    public void googleCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        // Échange code contre access_token
        RestTemplate restTemplate = new RestTemplate();
        String tokenUrl = "https://oauth2.googleapis.com/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("code", code);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, request, Map.class);

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        // Récupère l'email utilisateur
        String userInfoUrl = "https://openidconnect.googleapis.com/v1/userinfo";
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);
        ResponseEntity<Map> userResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userRequest, Map.class);

        String email = (String) userResponse.getBody().get("email");

        // Enregistre ou retrouve le client
        Client client = clientRepository.findByEmail(email);
        if (client == null) {
            client = new Client();
            client.setEmail(email);
            client.setPassword(""); // inutile pour OAuth
            clientRepository.save(client);
        }

        // Génère le JWT
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Secret));
        String jwt = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Redirige vers Angular avec le token dans l'URL
        response.sendRedirect("http://localhost:4200/login?token=" + jwt);
    }
}