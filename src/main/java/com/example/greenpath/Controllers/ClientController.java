package com.example.greenpath.Controllers;

import com.example.greenpath.Models.Avis;
import com.example.greenpath.Models.AvisRequest;
import com.example.greenpath.Models.Circuit;
import com.example.greenpath.Models.Client;
import com.example.greenpath.Repositories.AvisRepository;
import com.example.greenpath.Repositories.CircuitRepository;
import com.example.greenpath.Repositories.ClientRepository;
import com.example.greenpath.Services.ClientService;
import com.example.greenpath.Services.SuggestionAvanceeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CircuitRepository circuitRepository;
    @Autowired
    private AvisRepository avisRepository;
    @Autowired
    private SuggestionAvanceeService suggestionAvanceeService;
    @Autowired
    private ClientService clientService;

    @PostMapping("/{clientId}/favoris/{circuitId}")
    public ResponseEntity<?> ajouterAuxFavoris(@PathVariable Long clientId, @PathVariable Long circuitId) {
        Client client = clientRepository.findById(clientId).orElseThrow();
        Circuit circuit = circuitRepository.findById(circuitId).orElseThrow();

        client.getFavoris().add(circuit);
        clientRepository.save(client);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clientId}/favoris/{circuitId}")
    public ResponseEntity<?> retirerDesFavoris(@PathVariable Long clientId, @PathVariable Long circuitId) {
        Client client = clientRepository.findById(clientId).orElseThrow();
        Circuit circuit = circuitRepository.findById(circuitId).orElseThrow();

        client.getFavoris().remove(circuit);
        clientRepository.save(client);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{clientId}/favoris")
    public ResponseEntity<Set<Circuit>> listerFavoris(@PathVariable Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow();
        return ResponseEntity.ok(client.getFavoris());
    }
    @PutMapping("/clients/{id}/preferences")
    public ResponseEntity<Client> updateClientPreferences(@PathVariable Long id, @RequestBody Client prefs) {
        return clientRepository.findById(id).map(client -> {
            client.setNatureScore(prefs.getNatureScore());
            client.setCultureScore(prefs.getCultureScore());
            client.setSportScore(prefs.getSportScore());
            client.setEcoScore(prefs.getEcoScore());
            client.setDureePreferee(prefs.getDureePreferee());
            return ResponseEntity.ok(clientRepository.save(client));
        }).orElse(ResponseEntity.notFound().build());
    }



}
