package com.example.greenpath.Models;
import com.example.greenpath.Enum.CircuitStatus;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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
    @ManyToMany(mappedBy = "favoris")
    private Set<Client> favorisParClients = new HashSet<>();
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "circuit_tags", joinColumns = @JoinColumn(name = "circuit_id"))
    @Column(name = "tag")
    private Set<String> tags;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "circuit_lieux",
            joinColumns = @JoinColumn(name = "circuit_id"),
            inverseJoinColumns = @JoinColumn(name = "lieu_id")
    )
    private Set<Lieu> lieux = new HashSet<>();
    @ManyToOne(optional = true) // guide peut être null
    @JoinColumn(name = "guide_id")
    private Guide guide;
    @Enumerated(EnumType.STRING)
    private CircuitStatus status = CircuitStatus.PROPOSE;
    @ManyToOne
    @JoinColumn(name = "propose_par_client_id")
    private Client proposePar;

    public Client getProposePar() {
        return proposePar; }
    public void setProposePar(Client proposePar) {
        this.proposePar = proposePar; }

    public CircuitStatus getStatus() { return status; }
    public void setStatus(CircuitStatus status) { this.status = status; }

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

    public Set<Client> getFavorisParClients() {
        return favorisParClients;
    }

    public void setFavorisParClients(Set<Client> favorisParClients) {
        this.favorisParClients = favorisParClients;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Set<Lieu> getLieux() {
        return lieux;
    }

    public void setLieux(Set<Lieu> lieux) {
        this.lieux = lieux;
    }

    public Guide getGuide() {
        return guide;
    }

    public void setGuide(Guide guide) {
        this.guide = guide;
    }
}