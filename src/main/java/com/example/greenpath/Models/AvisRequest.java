package com.example.greenpath.Models;

public class AvisRequest {
    private Long clientId;
    private Long circuitId;
    private int note;
    private String commentaire;

    public AvisRequest(Long clientId, Long circuitId, int note, String commentaire) {
        this.clientId = clientId;
        this.circuitId = circuitId;
        this.note = note;
        this.commentaire = commentaire;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(Long circuitId) {
        this.circuitId = circuitId;
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
}
