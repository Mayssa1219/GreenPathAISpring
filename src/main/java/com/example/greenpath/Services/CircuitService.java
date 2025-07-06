package com.example.greenpath.Services;

import com.example.greenpath.Models.Circuit;
import com.example.greenpath.Repositories.CircuitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CircuitService {

    private final CircuitRepository circuitRepository;

    public CircuitService(CircuitRepository circuitRepository) {
        this.circuitRepository = circuitRepository;
    }

    public List<Circuit> getAllCircuits() {
        return circuitRepository.findAll();
    }

    public Optional<Circuit> getCircuitById(Long id) {
        return circuitRepository.findById(id);
    }

    public Circuit saveCircuit(Circuit circuit) {
        return circuitRepository.save(circuit);
    }

    public void deleteCircuit(Long id) {
        circuitRepository.deleteById(id);
    }

}