package com.example.greenpath.Repositories;


import com.example.greenpath.Models.Circuit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CircuitRepository extends JpaRepository<Circuit, Long> {
    // Méthodes personnalisées si besoin, ex :
    // List<Circuit> findByNiveauEcoresponsabilite(int niveau);
}
