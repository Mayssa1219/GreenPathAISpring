package com.example.greenpath.Models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "lieux")
public class Lieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column
    private String region; // Pour filtrer selon la localisation du client

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "lieu_tags", joinColumns = @JoinColumn(name = "lieu_id"))
    @Column(name = "tag")
    private Set<String> tags;

    // Constructeurs
    public Lieu() {}

    public Lieu(String nom, String region, Set<String> tags) {
        this.nom = nom;
        this.region = region;
        this.tags = tags;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }

}
