package org.regikeskus.events.controller;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.model.Individual;
import org.regikeskus.events.service.IndividualService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/individuals")
public class IndividualController {

    private final IndividualService individualService;

    @GetMapping
    public List<Individual> getAllIndividuals() {
        return individualService.getAllIndividuals();
    } // TODO verify if it is really needed. might be obsolete.

    @GetMapping("/{id}")
    public ResponseEntity<Individual> getIndividualById(@PathVariable Long id) {
        return individualService.getIndividualById(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @GetMapping("/event/{eventId}/individuals")
    public ResponseEntity<List<Individual>> getIndividualsByEventId(@PathVariable Long eventId) {
        List<Individual> individuals = individualService.getIndividualsByEventId(eventId);
        if (individuals.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(individuals);
    }

    @PostMapping
    public ResponseEntity<Individual> createIndividual(@RequestBody Individual individual) {
        Individual savedIndividual = individualService.createIndividual(individual);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIndividual);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Individual> updateIndividual(@PathVariable Long id, @RequestBody Individual individual) {
        try {
            Individual updatedIndividual = individualService.updateIndividual(id, individual);
            return ResponseEntity.ok(updatedIndividual);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteIndividual(@PathVariable Long id) {
        individualService.deleteIndividual(id);
        return ResponseEntity.ok().build();
    }
}
