package com.example.greenpath.Services;


import com.example.greenpath.Models.Circuit;
import com.example.greenpath.Models.Client;
import com.example.greenpath.Models.Lieu;
import com.example.greenpath.Repositories.ClientRepository;
import net.snowflake.client.jdbc.internal.amazonaws.services.kms.model.Tag;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
    private static final Set<String> TAGS_AUTORISES = Set.of(
            "nature", "patrimoine", "gastronomie", "sport", "bien-etre"
    );

    private static final double AJUSTEMENT = 0.2;
    private static final double MAX_SCORE = 10.0;
    public void ajusterPreference(Client client, String tag) {
        if (!TAGS_AUTORISES.contains(tag)) return; // Ignorer les tags inconnus

        switch (tag) {
            case "nature" -> client.setNatureScore(Math.min(client.getNatureScore() + AJUSTEMENT, MAX_SCORE));
            case "patrimoine", "gastronomie" -> client.setCultureScore(Math.min(client.getCultureScore() + AJUSTEMENT, MAX_SCORE));
            case "sport" -> client.setSportScore(Math.min(client.getSportScore() + AJUSTEMENT, MAX_SCORE));
            case "bien-etre" -> client.setEcoScore(Math.min(client.getEcoScore() + AJUSTEMENT, MAX_SCORE));
        }
    }
    public void reduirePreference(Client client, String tag) {
        if (!TAGS_AUTORISES.contains(tag)) return; // Ignore les tags inconnus

        switch (tag.toLowerCase()) {
            case "nature" -> client.setNatureScore(Math.max(client.getNatureScore() - AJUSTEMENT, 0));
            case "patrimoine", "gastronomie" -> client.setCultureScore(Math.max(client.getCultureScore() - AJUSTEMENT, 0));
            case "sport" -> client.setSportScore(Math.max(client.getSportScore() - AJUSTEMENT, 0));
            case "bien-etre" -> client.setEcoScore(Math.max(client.getEcoScore() - AJUSTEMENT, 0));
        }
    }

    public void diminuerPreferencesSelonCircuit(Client client, Circuit circuit) {
        for (String tag : circuit.getTags()) {
            reduirePreference(client, tag); // centralisé ici
        }
        clientRepository.save(client);
    }

    public void ajusterPreferencesSelonCircuit(Client client, Circuit circuit) {
        for (Lieu lieu : circuit.getLieux()) {
            for (String tag : lieu.getTags()) {
                ajusterPreference(client, tag); // +0.2 par tag
            }
        }
        clientRepository.save(client);
    }






}