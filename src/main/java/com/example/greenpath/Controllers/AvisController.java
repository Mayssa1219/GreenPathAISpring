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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/avis")
public class AvisController {

    private final ClientRepository clientRepository;
    private final CircuitRepository circuitRepository;
    private final AvisRepository avisRepository;
    private final SuggestionAvanceeService suggestionService;
    private final ClientService clientService;

    public AvisController(ClientRepository clientRepo, CircuitRepository circuitRepo,
                          AvisRepository avisRepo, SuggestionAvanceeService suggestionService, ClientService clientService) {
        this.clientRepository = clientRepo;
        this.circuitRepository = circuitRepo;
        this.avisRepository = avisRepo;
        this.suggestionService = suggestionService;
        this.clientService = clientService;
    }

    @PostMapping("/ajouter")
    public ResponseEntity<Avis> ajouterAvis(@RequestBody AvisRequest request) {
        Client client = clientRepository.findById(request.getClientId()).orElse(null);
        Circuit circuit = circuitRepository.findById(request.getCircuitId()).orElse(null);

        if (client == null || circuit == null) return ResponseEntity.notFound().build();

        Avis avis = new Avis();
        avis.setClient(client);
        avis.setCircuit(circuit);
        avis.setNote(request.getNote());
        avis.setCommentaire(request.getCommentaire());
        avis.setDateAvis(LocalDate.now());
        avisRepository.save(avis);

        // 💡 Ajustement automatique selon la note
        if (request.getNote() >= 4) {
            clientService.ajusterPreferencesSelonCircuit(client, circuit);
        } else if (request.getNote() <= 2) {
            clientService.diminuerPreferencesSelonCircuit(client, circuit);
        }

        return ResponseEntity.ok(avis);
    }
    @DeleteMapping("/supprimer/{id}")
    public ResponseEntity<Void> supprimerAvis(@PathVariable Long id) {
        if (!avisRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        avisRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


}

