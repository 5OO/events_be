package org.regikeskus.events.service;

import org.regikeskus.events.model.Event;
import org.regikeskus.events.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Transactional
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
