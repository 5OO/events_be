package org.regikeskus.events.service;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.dto.EventDTO;
import org.regikeskus.events.dto.EventWithParticipantsDTO;
import org.regikeskus.events.exception.EventNotFoundException;
import org.regikeskus.events.exception.EventValidationException;
import org.regikeskus.events.model.Company;
import org.regikeskus.events.model.Event;
import org.regikeskus.events.model.Individual;
import org.regikeskus.events.repository.CompanyRepository;
import org.regikeskus.events.repository.EventRepository;
import org.regikeskus.events.repository.IndividualRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EventService {

    private static final String EVENT_NOT_FOUND_MESSAGE = "Event not found with event-ID: ";

    private final EventRepository eventRepository;
    private final IndividualRepository individualRepository;
    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<EventWithParticipantsDTO> getAllFutureOrCurrentEvents() {
        List<Event> eventList = eventRepository.findAllFutureOrCurrentEvents(LocalDateTime.now());
        return eventList.stream().map(this::mapToEventWithParticipantsDTO).collect(Collectors.toList());
    }

    private EventWithParticipantsDTO mapToEventWithParticipantsDTO(Event event) {
        long individualCount = individualRepository.countByEventId(event.getEventId());
        Long companyParticipantsSum = companyRepository.sumNumberOfParticipantsByEventId(event.getEventId());
        long totalCompanyParticipants = companyParticipantsSum != null ? companyParticipantsSum : 0;
        int totalParticipants = (int) (individualCount + totalCompanyParticipants);
        return new EventWithParticipantsDTO(
                event.getEventId(),
                event.getEventName(),
                event.getEventDateTime().toLocalDate(),
                totalParticipants
        );
    }

    @Transactional(readOnly = true)
    public List<EventDTO> getAllPastEvents() {
        return eventRepository.findAllPastEvents(LocalDateTime.now()).stream()
                .map(this::mapToEventDTO)
                .collect(Collectors.toList());

    }



    private EventDTO mapToEventDTO(Event event) {
        return new EventDTO(
                event.getEventId(),
                event.getEventName(),
                event.getEventDateTime(),
                event.getLocation(),
                event.getAdditionalInfo()
        );
    }

    @Transactional(readOnly = true)
    public Optional<Event> getEventById(Long id) {
        return Optional.ofNullable(eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(EVENT_NOT_FOUND_MESSAGE + id)));
    }

    @Transactional
    public Event createEvent(Event event) {
        validateEvent(event);
        return eventRepository.save(event);
    }

    @Transactional
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NoSuchElementException(EVENT_NOT_FOUND_MESSAGE + id));

        event.setEventName(eventDetails.getEventName());
        event.setEventDateTime(eventDetails.getEventDateTime());
        event.setLocation(eventDetails.getLocation());
        event.setAdditionalInfo(eventDetails.getAdditionalInfo());

        validateEvent(event);
        return eventRepository.save(event);
    }

    private void validateEvent(Event event) {
        if (event.getEventName() == null || event.getEventDateTime() == null || event.getLocation() == null) {
            throw new EventValidationException("Event name or date/time or location must not be null");
        }
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NoSuchElementException(EVENT_NOT_FOUND_MESSAGE + id));

        if (event.getEventDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot delete past events.");
        }
        List<Individual> individuals = individualRepository.findByEventId(id);
        List<Company> companies = companyRepository.findByEventId(id);

        individualRepository.deleteAll(individuals);
        companyRepository.deleteAll(companies);
        eventRepository.deleteById(id);
    }
}
