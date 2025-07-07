package com.example.greenpath.Models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Avis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int note; // 1 à 5
    private String commentaire;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Circuit circuit;

    private LocalDate dateAvis = LocalDate.now();

    public Avis(Long id, int note, String commentaire, Client client, Circuit circuit, LocalDate dateAvis) {
        this.id = id;
        this.note = note;
        this.commentaire = commentaire;
        this.client = client;
        this.circuit = circuit;
        this.dateAvis = dateAvis;
    }

    public Avis() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    public LocalDate getDateAvis() {
        return dateAvis;
    }

    public void setDateAvis(LocalDate dateAvis) {
        this.dateAvis = dateAvis;
    }
}
