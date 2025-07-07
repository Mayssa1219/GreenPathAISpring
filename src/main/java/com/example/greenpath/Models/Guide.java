package com.example.greenpath.Models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "guides")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;
    private String motDePasse;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL)
    private Set<Circuit> circuitsCrees = new HashSet<>();

    public Guide(Long id, String nom, String email, String motDePasse, Set<Circuit> circuitsCrees) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.circuitsCrees = circuitsCrees;
    }

    public Guide() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Set<Circuit> getCircuitsCrees() {
        return circuitsCrees;
    }

    public void setCircuitsCrees(Set<Circuit> circuitsCrees) {
        this.circuitsCrees = circuitsCrees;
    }

}
