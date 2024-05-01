package org.regikeskus.events.controller;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.model.Individual;
import org.regikeskus.events.service.IndividualService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/individuals")
public class IndividualController {

    private final IndividualService individualService;

    @GetMapping
    public List<Individual> getAllIndividuals() {
        return individualService.getAllIndividuals();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Individual> getIndividualById(@PathVariable Long id) {
        return individualService.getIndividualById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Individual> createIndividual(@RequestBody Individual individual) {
        Individual savedIndividual = individualService.createOrUpdateIndividual(individual);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIndividual);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Individual> updateIndividual(@PathVariable Long id, @RequestBody Individual individual) {
        return individualService.getIndividualById(id).map(savedIndividual -> {
            savedIndividual.setFirstName(individual.getFirstName());
            savedIndividual.setLastName(individual.getLastName());
            savedIndividual.setPersonalID(individual.getPersonalID());
            savedIndividual.setPaymentMethod(individual.getPaymentMethod());
            savedIndividual.setAdditionalInfo(individual.getAdditionalInfo());
            Individual updatedIndividual = individualService.createOrUpdateIndividual(savedIndividual);
            return ResponseEntity.ok(updatedIndividual);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteIndividual(@PathVariable Long id) {
        return individualService.getIndividualById(id).map(ignored -> {
            individualService.deleteIndividual(id);
            return ResponseEntity.ok().build();
        }).orElseGet(()-> ResponseEntity.notFound().build());
    }
}
