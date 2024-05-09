package org.regikeskus.events.service;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.dto.AggregatedParticipantDTO;
import org.regikeskus.events.dto.EventDTO;
import org.regikeskus.events.dto.EventWithAggregatedParticipantsDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EventService {

    private static final String EVENT_NOT_FOUND_MESSAGE = "Event not found with event-ID: ";

    private final EventRepository eventRepository;
    private final IndividualRepository individualRepository;
    private final CompanyRepository companyRepository;

    public EventWithAggregatedParticipantsDTO getEventWithAggregatedParticipants(Long eventId) {
        var event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + eventId));

        List<AggregatedParticipantDTO> participants = new ArrayList<>();

        individualRepository.findByEventId(eventId).forEach(individual -> {
            AggregatedParticipantDTO participantDTO = new AggregatedParticipantDTO(
                    individual.getParticipantId(),
                    "individual",
                    individual.getFirstName() + " " + individual.getLastName(),
                    individual.getPersonalID()
            );
            participants.add(participantDTO);
        });

        companyRepository.findByEventId(eventId).forEach(company -> {
            AggregatedParticipantDTO participantDTO = new AggregatedParticipantDTO(
                    company.getParticipantId(),
                    "company",
                    company.getLegalName(),
                    company.getRegistrationCode()
            );
            participants.add(participantDTO);
        });

        return new EventWithAggregatedParticipantsDTO(
                event.getEventId(),
                event.getEventName(),
                event.getEventDateTime().toLocalDate(),
                event.getLocation(),
                participants
        );
    }

    @Transactional(readOnly = true)
    public List<EventWithParticipantsDTO> getAllFutureOrCurrentEvents() {
        List<Event> eventList = eventRepository.findAllFutureOrCurrentEvents(LocalDateTime.now());
        return eventList.stream().map(this::mapToEventWithParticipantsDTO).toList();
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
                .map(this::mapToEventDTO).toList();

    }

    @Transactional(readOnly = true)
    public Optional<EventDTO> getEventById(Long id) {
        return eventRepository.findById(id)
                .map(this::mapToEventDTO);
    }

    @Transactional
    public EventDTO createEvent(EventDTO eventDTO) {
        validateEvent(eventDTO);
        Event event = mapToEventEntity(eventDTO);
         Event savedEvent = eventRepository.save(event);
        return mapToEventDTO(savedEvent);
    }

    @Transactional
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new NoSuchElementException(EVENT_NOT_FOUND_MESSAGE + id));
        validateEvent(eventDTO);
        event.setEventName(eventDTO.getEventName());
        event.setEventDateTime(eventDTO.getEventDateTime());
        event.setLocation(eventDTO.getLocation());
        event.setAdditionalInfo(eventDTO.getAdditionalInfo());
        Event updatedEvent = eventRepository.save(event);
        return mapToEventDTO(updatedEvent);
    }

    private void validateEvent(EventDTO event) {
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

    private EventDTO mapToEventDTO(Event event) {
        return new EventDTO(
                event.getEventId(),
                event.getEventName(),
                event.getEventDateTime(),
                event.getLocation(),
                event.getAdditionalInfo()
        );
    }

    private Event mapToEventEntity(EventDTO eventDTO) {
        return new Event(
                eventDTO.getEventId(),
                eventDTO.getEventName(),
                eventDTO.getEventDateTime(),
                eventDTO.getLocation(),
                eventDTO.getAdditionalInfo()
        );
    }
}
