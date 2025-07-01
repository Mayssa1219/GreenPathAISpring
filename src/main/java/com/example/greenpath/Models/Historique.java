package com.example.greenpath.Models;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historiques")
public class Historique {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "circuit_id")
    private Circuit circuit;

    private LocalDateTime date;

    public Historique(Long id, Client client, Circuit circuit, LocalDateTime date) {
        this.id = id;
        this.client = client;
        this.circuit = circuit;
        this.date = date;
    }

    public Historique() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}