package com.example.greenpath.Services;

import com.example.greenpath.Models.Circuit;
import com.example.greenpath.Models.Client;
import com.example.greenpath.Models.Lieu;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SuggestionIAService {

        public List<Circuit> suggérerCircuitsPourClient(Client client, List<Circuit> circuits, int nbSuggestions) {
            return circuits.stream()
                    .map(c -> Map.entry(c, calculerDistance(client, c)))
                    .sorted(Comparator.comparingDouble(Map.Entry::getValue))
                    .limit(nbSuggestions)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

    private double calculerDistance(Client client, Circuit circuit) {
        Set<String> clientPrefs = client.getPreferences() != null ? client.getPreferences() : Set.of();
        Set<String> circuitTags = circuit.getTags() != null ? circuit.getTags() : Set.of();

        long nbCommunsTags = circuitTags.stream()
                .filter(clientPrefs::contains)
                .count();

        int totalTags = circuitTags.size() > 0 ? circuitTags.size() : 1;
        double distanceTags = 1 - ((double) nbCommunsTags / totalTags);

        // Durée
        double dureeClient = client.getDureePreferee() != 0 ? client.getDureePreferee() : 12;
        double distanceDuree = Math.abs(normalize(dureeClient) - normalize(circuit.getDuree()));

        // Lieux visités
        Set<Lieu> lieuxClient = client.getLieuxVisites() != null ? client.getLieuxVisites() : Set.of();
        Set<Lieu> lieuxCircuit = circuit.getLieux() != null ? circuit.getLieux() : Set.of();

        // Nombre de lieux déjà visités dans ce circuit
        long nbLieuxVisitesEnCommun = lieuxCircuit.stream()
                .filter(lieuxClient::contains)
                .count();

        // On peut définir la distance liée aux lieux comme la proportion de lieux NOUVEAUX (non visités)
        double distanceLieux = 1 - ((double) nbLieuxVisitesEnCommun / Math.max(lieuxCircuit.size(), 1));

        // Poids pour chaque critère
        double poidsTags = 0.5;
        double poidsDuree = 0.3;
        double poidsLieux = 0.2;

        return poidsTags * distanceTags + poidsDuree * distanceDuree + poidsLieux * distanceLieux;
    }


    private double normalize(double value) {
        return value / 24.0; // Par exemple, durée max = 24h
    }


}
