package org.regikeskus.events.controller;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.dto.EventDTO;
import org.regikeskus.events.dto.EventWithParticipantsDTO;
import org.regikeskus.events.model.Event;
import org.regikeskus.events.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @GetMapping("/future")
    public ResponseEntity<List<EventWithParticipantsDTO>> getAllFutureOrCurrentEvents() {
        List<EventWithParticipantsDTO> events = eventService.getAllFutureOrCurrentEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/past")
    public ResponseEntity<List<EventDTO>> getAllPastEvents() {
        List<EventDTO> events = eventService.getAllPastEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Object> createEvent(@RequestBody Event event) {
            Event createdEvent = eventService.createEvent(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
            Event updatedEvent = eventService.updateEvent(id, eventDetails);
            return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }
}
