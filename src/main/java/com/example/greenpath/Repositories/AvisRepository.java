package com.example.greenpath.Repositories;

import com.example.greenpath.Models.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisRepository extends JpaRepository<Avis, Long> {
    // Custom query methods can be defined here if needed
}
