package com.example.greenpath.Controllers;

import com.example.greenpath.Enum.CircuitStatus;
import com.example.greenpath.Models.Circuit;
import com.example.greenpath.Models.CircuitRequest;
import com.example.greenpath.Models.Client;
import com.example.greenpath.Models.Guide;
import com.example.greenpath.Repositories.CircuitRepository;
import com.example.greenpath.Repositories.ClientRepository;
import com.example.greenpath.Repositories.GuideRepository;
import com.example.greenpath.Services.SuggestionAvanceeService;
import com.example.greenpath.Services.SuggestionIAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private GuideRepository guideRepository;
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
    @PostMapping("/ajouter")
    public ResponseEntity<Circuit> ajouterCircuit(@RequestBody Circuit circuit) {
        if (circuit.getTitre() == null || circuit.getTitre().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Circuit savedCircuit = circuitRepository.save(circuit);
        return ResponseEntity.ok(savedCircuit);
    }
    @PostMapping("/proposer")
    public ResponseEntity<Circuit> proposerCircuit(@RequestBody CircuitRequest request) {
        Client client = clientRepository.findById(request.getClientId()).orElse(null);
        if (client == null) return ResponseEntity.notFound().build();

        Circuit circuit = new Circuit();
        circuit.setTitre(request.getTitre());
        circuit.setDescription(request.getDescription());
        circuit.setEtapes(request.getEtapes());
        circuit.setDuree(request.getDuree());
        circuit.setNiveauEcoresponsabilite(request.getNiveauEcoresponsabilite());
        circuit.setTags(request.getTags());

        circuit.setProposePar(client);
        circuit.setStatus(CircuitStatus.PROPOSE);

        circuitRepository.save(circuit);

        return ResponseEntity.ok(circuit);
    }
    @PostMapping("/ajouter")
    public ResponseEntity<Circuit> ajouterCircuit(@RequestBody CircuitRequest request) {
        Circuit circuit = new Circuit();
        circuit.setTitre(request.getTitre());
        circuit.setDescription(request.getDescription());
        circuit.setEtapes(request.getEtapes());
        circuit.setDuree(request.getDuree());
        circuit.setNiveauEcoresponsabilite(request.getNiveauEcoresponsabilite());
        circuit.setTags(request.getTags());

        if (request.getGuide() != null) {
            Guide guide = guideRepository.findById(request.getGuide()).orElse(null);
            if (guide == null) {
                return ResponseEntity.badRequest().body(null);
            }
            circuit.setGuide(guide);
        }

        circuitRepository.save(circuit);
        return ResponseEntity.ok(circuit);
    }
}

