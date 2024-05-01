package org.regikeskus.events.service;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.model.Event;
import org.regikeskus.events.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Transactional
    public Event createOrUpdateEvent(Event event) {
        if (event.getEventName() == null || event.getEventDateTime() == null || event.getLocation() == null) {
            throw new IllegalArgumentException("Event name or date/time or location must not be null");  }
        return eventRepository.save(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
