package com.example.greenpath.Models;
import jakarta.persistence.*;

@Entity
@Table(name = "circuits")
public class Circuit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private String etapes; // JSON ou texte séparé (adapter selon besoin)
    private int duree; // en heures ou jours
    private int niveauEcoresponsabilite;

    // Constructeurs
    public Circuit() {}

    public Circuit(String titre, String description, String etapes, int duree, int niveauEcoresponsabilite) {
        this.titre = titre;
        this.description = description;
        this.etapes = etapes;
        this.duree = duree;
        this.niveauEcoresponsabilite = niveauEcoresponsabilite;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getEtapes() { return etapes; }
    public void setEtapes(String etapes) { this.etapes = etapes; }
    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }
    public int getNiveauEcoresponsabilite() { return niveauEcoresponsabilite; }
    public void setNiveauEcoresponsabilite(int niveauEcoresponsabilite) { this.niveauEcoresponsabilite = niveauEcoresponsabilite; }
}