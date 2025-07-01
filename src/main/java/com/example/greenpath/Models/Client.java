package com.example.greenpath.Models;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullname;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String photoUrl = "https://res.cloudinary.com/ds9vfy7vv/image/upload/v1751041140/user-portable-network-graphics-computer-icons-clip-art-transparency-png-favpng-swCuLrqVtJsSnYeU1x9eFKEv1_wvtoeh.jpg";
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_preferences", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "preference")
    private Set<String> preferences;

    @Column(nullable = false)
    private String password;
    @Column(nullable = true)
    private String statut="en attente";

    public Client() {}

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Client(String fullname, String email, String location, Set<String> preferences, String password, String statut) {
        this.fullname = fullname;
        this.email = email;
        this.location = location;
        this.preferences = preferences;
        this.password = password;
        this.statut = statut;
        this.photoUrl = "https://res.cloudinary.com/ds9vfy7vv/image/upload/v1751041140/user-portable-network-graphics-computer-icons-clip-art-transparency-png-favpng-swCuLrqVtJsSnYeU1x9eFKEv1_wvtoeh.jpg";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Set<String> getPreferences() { return preferences; }
    public void setPreferences(Set<String> preferences) { this.preferences = preferences; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}