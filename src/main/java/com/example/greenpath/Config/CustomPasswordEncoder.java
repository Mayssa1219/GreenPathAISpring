package com.example.greenpath.Config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomPasswordEncoder {

    /**
     * Encode le mot de passe brut en SHA-256 hexadécimal.
     * @param rawPassword mot de passe brut
     * @return hash SHA-256 en hexadécimal
     */
    public String encode(String rawPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    /**
     * Vérifie si le mot de passe brut correspond au hash encodé.
     * @param rawPassword mot de passe brut
     * @param encodedPassword hash attendu
     * @return true si correspond, false sinon
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return encode(rawPassword).equalsIgnoreCase(encodedPassword);
    }

    // Outil pour convertir un tableau de bytes en hexadécimal
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}