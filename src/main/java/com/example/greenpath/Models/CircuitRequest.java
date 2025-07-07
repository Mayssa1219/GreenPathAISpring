package com.example.greenpath.Models;


import java.util.Set;

public class CircuitRequest {
    private Long clientId;
    private String titre;
    private String description;
    private String etapes;
    private int duree;
    private Guide guide; // Guide optionnel, peut être null
    private int niveauEcoresponsabilite;
    private Set<String> tags;

    public CircuitRequest() {}

    // Getters et setters
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
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
    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }
}

