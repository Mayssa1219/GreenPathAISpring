package com.example.greenpath.Models;

import jakarta.persistence.*;

@Entity
@Table(name = "partenaires")
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, unique = true)
    private String email;

    private String activite;
    private String localisation;
    private String description;
    private String logo;

    public Partner() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Partner(String nom, String email, String activite, String localisation, String description, String logo) {
        this.nom = nom;
        this.email = email;
        this.activite = activite;
        this.localisation = localisation;
        this.description = description;
        this.logo = logo;
    }

    // Getters et setters (génère-les avec ton IDE ou Lombok)
    public Long getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getActivite() { return activite; }
    public void setActivite(String activite) { this.activite = activite; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
}