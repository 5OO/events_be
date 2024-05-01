package org.regikeskus.events.controller;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.model.Event;
import org.regikeskus.events.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.createOrUpdateEvent(event);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return eventService.getEventById(id).map(savedEvent -> {
            savedEvent.setEventName(eventDetails.getEventName());
            savedEvent.setEventDateTime(eventDetails.getEventDateTime());
            savedEvent.setLocation(eventDetails.getLocation());
            savedEvent.setAdditionalInfo(eventDetails.getAdditionalInfo());
            Event updatedEvent = eventService.createOrUpdateEvent(savedEvent);
            return ResponseEntity.ok(updatedEvent);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        return eventService.getEventById(id).map(event -> {
            eventService.deleteEvent(id);
            return ResponseEntity.ok().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
