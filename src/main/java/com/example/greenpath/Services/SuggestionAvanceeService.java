package com.example.greenpath.Services;


import com.example.greenpath.Models.Circuit;
import com.example.greenpath.Models.Client;
import com.example.greenpath.Models.Lieu;
import com.example.greenpath.Repositories.LieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SuggestionAvanceeService {

    @Autowired
    private LieuRepository lieuRepository;

    public Circuit genererCircuitPersonnalise(Client client, String localisation) {
        List<Lieu> lieuxDansRegion = lieuRepository.findByRegion(localisation);

        // Trier les scores du client par ordre décroissant
        Map<String, Double> scores = Map.of(
                "nature", client.getNatureScore(),
                "culture", client.getCultureScore(),
                "sport", client.getSportScore(),
                "eco", client.getEcoScore()
        );

        List<String> prefsTriees = scores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Filtrer les lieux qui ont les tags préférés
        List<Lieu> lieuxFiltres = lieuxDansRegion.stream()
                .filter(lieu -> prefsTriees.stream().anyMatch(tag -> lieu.getTags().contains(tag)))
                .limit(5) // max 5 lieux pour circuit
                .collect(Collectors.toList());

        if (lieuxFiltres.isEmpty()) return null;

        // Créer un circuit personnalisé (non persistant)
        Circuit circuit = new Circuit();
        circuit.setTitre("Découverte autour de " + localisation);
        circuit.setDescription("Un circuit généré selon vos préférences 🌿");
        circuit.setDuree(lieuxFiltres.size()); // 1h/lieu simplifié
        circuit.setNiveauEcoresponsabilite(3); // valeur par défaut
        circuit.setLieux(new HashSet<>(lieuxFiltres));
        circuit.setTags(prefsTriees.stream().limit(3).collect(Collectors.toSet()));

        return circuit;
    }

}
