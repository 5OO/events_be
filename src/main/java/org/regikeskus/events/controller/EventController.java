package org.regikeskus.events.controller;

import org.regikeskus.events.model.Event;
import org.regikeskus.events.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

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
        return eventService.saveEvent(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        return eventService.getEventById(id).map(savedEvent -> {
            savedEvent.setEventName(eventDetails.getEventName());
            savedEvent.setEventDateTime(eventDetails.getEventDateTime());
            savedEvent.setLocation(eventDetails.getLocation());
            savedEvent.setAdditionalInfo(eventDetails.getAdditionalInfo());
            Event updatedEvent = eventService.saveEvent(savedEvent);
            return ResponseEntity.ok(updatedEvent);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        return eventService.getEventById(id).map(event -> {
            eventService.deleteEvent(event.getEventId());
            return ResponseEntity.ok().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
