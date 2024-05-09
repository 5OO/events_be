package org.regikeskus.events.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.regikeskus.events.dto.EventDTO;
import org.regikeskus.events.dto.EventWithParticipantsDTO;
import org.regikeskus.events.exception.EventNotFoundException;
import org.regikeskus.events.exception.EventValidationException;
import org.regikeskus.events.model.Event;
import org.regikeskus.events.repository.CompanyRepository;
import org.regikeskus.events.repository.EventRepository;
import org.regikeskus.events.repository.IndividualRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private IndividualRepository individualRepository;

    @Mock
    private CompanyRepository companyRepository;

    private EventDTO eventDTO;
    private Event event;

    @BeforeEach
    void setup() {
        eventDTO = new EventDTO(1L, "Event name", LocalDateTime.now(), "Event location", "Additional info");
        event = new Event(1L, "Event name", LocalDateTime.now(), "Event location", "Additional info");
    }

    @Test
    void testGetAllFutureOrCurrentEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event(1L, "Event name 1", LocalDateTime.now().plusDays(1), "Event location 1", "Additional info 1"));
        events.add(new Event(2L, "Event name 2", LocalDateTime.now().plusDays(2), "Event location 2", "Additional info 2"));
        when(eventRepository.findAllFutureOrCurrentEvents(any(LocalDateTime.class))).thenReturn(events);
        when(individualRepository.countByEventId(anyLong())).thenReturn(2L);
        when(companyRepository.sumNumberOfParticipantsByEventId(anyLong())).thenReturn(3L);

        List<EventWithParticipantsDTO> retrievedEvents = eventService.getAllFutureOrCurrentEvents();

        assertNotNull(retrievedEvents);
        assertEquals(2, retrievedEvents.size());
        assertEquals(5, retrievedEvents.getFirst().getTotalParticipants());
        verify(eventRepository).findAllFutureOrCurrentEvents(any(LocalDateTime.class));
        verify(individualRepository, times(2)).countByEventId(anyLong());
        verify(companyRepository, times(2)).sumNumberOfParticipantsByEventId(anyLong());
    }

    @Test
    void testGetAllPastEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event(1L, "Past Event", LocalDateTime.now().minusDays(1), "Past Location", "Details"));
        events.add(new Event(2L, "Past Event 2", LocalDateTime.now().minusDays(2), "Past Location 2", "Details 2"));
        when(eventRepository.findAllPastEvents(any(LocalDateTime.class))).thenReturn(events);

        List<EventDTO> retrievedEvents = eventService.getAllPastEvents();

        assertNotNull(retrievedEvents);
        assertEquals(2, retrievedEvents.size());
        assertEquals("Past Event", retrievedEvents.getFirst().getEventName());
        verify(eventRepository).findAllPastEvents(any(LocalDateTime.class));
    }

    @Test
    void testGetEventById_Exists() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<EventDTO> foundEvent = eventService.getEventById(1L);

        assertTrue(foundEvent.isPresent());
        assertEquals("Event name", foundEvent.get().getEventName());
        verify(eventRepository).findById(1L);
    }

    @Test
    void testGetEventById_NotFound() {
        Long id = 1L;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EventNotFoundException.class, () -> {
            Optional<EventDTO> eventDTO = eventService.getEventById(id);
            if (eventDTO.isEmpty()) {
                throw new EventNotFoundException("Event not found with event-ID: " + id);
            }
        });

        assertEquals("Event not found with event-ID: " + id, exception.getMessage());
        verify(eventRepository).findById(1L);
    }

    @Test
    void testCreateEvent_Success() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventDTO savedEvent = eventService.createEvent(eventDTO);

        assertNotNull(savedEvent);
        assertEquals("Event name", savedEvent.getEventName());
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testCreateEvent_Failure() {
        EventDTO invalidEventDTO = new EventDTO(1L, null, LocalDateTime.now(), "Event location", "Additional info");

        Exception exception = assertThrows(EventValidationException.class, () -> eventService.createEvent(invalidEventDTO));

        assertEquals("Event name or date/time or location must not be null", exception.getMessage());
    }

    @Test
    void testUpdateEvent_Exists() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        EventDTO updatedEventDTO = new EventDTO(1L, "Updated name", LocalDateTime.now(), "Updated location", "Updated info");
        EventDTO updatedEvent = eventService.updateEvent(1L, updatedEventDTO);

        assertNotNull(updatedEvent);
        assertEquals("Updated name", updatedEvent.getEventName());
        verify(eventRepository).findById(1L);
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void testUpdateEvent_NotFound() {
        Long id = 1L;
        EventDTO updatedEventDTO = new EventDTO(1L, "Updated name", LocalDateTime.now(), "Updated location", "Updated info");
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> eventService.updateEvent(id, updatedEventDTO));

        assertEquals("Event not found with event-ID: " + id, exception.getMessage());
        verify(eventRepository).findById(id);
    }

    @Test
    void testDeleteEvent_FutureEvent() {
        Long id = 1L;
        Event futureEvent = new Event(1L, "Future Event", LocalDateTime.now().plusDays(1), "Future Location", "Details");
        when(eventRepository.findById(id)).thenReturn(Optional.of(futureEvent));

        eventService.deleteEvent(id);

        verify(eventRepository).deleteById(id);
    }

    @Test
    void testDeleteEvent_PastEvent() {
        Long id = 1L;
        Event pastEvent = new Event(1L, "Past Event", LocalDateTime.now().minusDays(1), "Past Location", "Details");
        when(eventRepository.findById(id)).thenReturn(Optional.of(pastEvent));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> eventService.deleteEvent(id));

        assertEquals("Cannot delete past events.", exception.getMessage());
        verify(eventRepository, never()).deleteById(any());
    }
}
