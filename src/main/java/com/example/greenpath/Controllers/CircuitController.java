package com.example.greenpath.Controllers;

import com.example.greenpath.Models.Circuit;
import com.example.greenpath.Models.Client;
import com.example.greenpath.Repositories.CircuitRepository;
import com.example.greenpath.Repositories.ClientRepository;
import com.example.greenpath.Services.SuggestionAvanceeService;
import com.example.greenpath.Services.SuggestionIAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class CircuitController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CircuitRepository circuitRepository;
    @Autowired
    private SuggestionIAService suggestionIAService;
    @Autowired
    private SuggestionAvanceeService suggestionAvanceeService;
    @GetMapping("/suggestions/{clientId}")
    public ResponseEntity<List<Circuit>> getSuggestions(@PathVariable Long clientId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Client client = clientOpt.get();
        List<Circuit> allCircuits = circuitRepository.findAll();

        List<Circuit> suggestions = suggestionIAService.suggérerCircuitsPourClient(client, allCircuits, 3);
        return ResponseEntity.ok(suggestions);
    }
    @GetMapping("/suggestions/personnalise/{clientId}/{localisation}")
    public ResponseEntity<Circuit> getCircuitPersonnalise(
            @PathVariable Long clientId,
            @PathVariable String localisation) {
        Client client = clientRepository.findById(clientId).orElse(null);
        if (client == null) return ResponseEntity.notFound().build();

        Circuit circuit = suggestionAvanceeService.genererCircuitPersonnalise(client, localisation);
        if (circuit == null) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(circuit);
    }


}

