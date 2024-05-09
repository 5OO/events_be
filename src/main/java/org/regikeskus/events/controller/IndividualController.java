package org.regikeskus.events.controller;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.dto.IndividualDTO;
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

    @GetMapping("/{participantId}/event/{eventId}")
    public ResponseEntity<IndividualDTO> getIndividualByIdAndEventId(@PathVariable Long participantId, @PathVariable Long eventId) {
        return ResponseEntity.ok(individualService.getIndividualByIdAndEventId(participantId, eventId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IndividualDTO> getIndividualById(@PathVariable Long id) {
        IndividualDTO individualDTO = individualService.getIndividualById(id);
        return ResponseEntity.ok(individualDTO);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<IndividualDTO>> getIndividualsByEventId(@PathVariable Long eventId) {
        List<IndividualDTO> individualDTOList = individualService.getIndividualsByEventId(eventId);
        if (individualDTOList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(individualDTOList);
    }

    @PostMapping
    public ResponseEntity<IndividualDTO> createIndividual(@RequestBody IndividualDTO individualDTO) {
        IndividualDTO savedIndividual = individualService.createIndividual(individualDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIndividual);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IndividualDTO> updateIndividual(@PathVariable Long id, @RequestBody IndividualDTO individualDTO) {
        IndividualDTO updatedIndividual = individualService.updateIndividual(id, individualDTO);
        return ResponseEntity.ok(updatedIndividual);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteIndividual(@PathVariable Long id) {
        individualService.deleteIndividual(id);
        return ResponseEntity.ok().build();
    }
}
