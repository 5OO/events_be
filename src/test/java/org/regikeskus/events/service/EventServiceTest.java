package org.regikeskus.events.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.regikeskus.events.exception.EventNotFoundException;
import org.regikeskus.events.exception.EventValidationException;
import org.regikeskus.events.model.Event;
import org.regikeskus.events.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    void testGetAllEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event(1L,"Event name 1",LocalDateTime.now(),"Event location 1" ,"Additional info 1"));
        events.add(new Event(2L, "Event name 2",LocalDateTime.now(),"Event location 2" ,"Additional info 2"));
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> retrievedEvents = eventService.getAllEvents();

        assertNotNull(retrievedEvents);
        assertEquals(2, retrievedEvents.size());
        assertEquals(events, retrievedEvents);
        verify(eventRepository).findAll();
    }

    @Test
    void testGetEventById_Exists() {
        Optional<Event> event = Optional.of(new Event(1L,"Event name 1",LocalDateTime.now(),"Event location 1" ,"Additional info 1"));
        when(eventRepository.findById(1L)).thenReturn(event);

        Optional<Event> foundEvent = eventService.getEventById(1L);

        assertTrue(foundEvent.isPresent());
        assertEquals("Event name 1", foundEvent.get().getEventName());
        assertEquals("Event location 1", foundEvent.get().getLocation());
        verify(eventRepository).findById(1L);
    }

    @Test
    void testGetEventById_NotFound() {
        Long id = 1L;
        when(eventRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EventNotFoundException.class, () -> eventService.getEventById(id));

        assertEquals("Event not found with ID: " + id, exception.getMessage());
        verify(eventRepository).findById(1L);
    }

    @Test
    void testCreateEvent_Success() {
        Event event = new Event(1L,"Event name 1",LocalDateTime.now(),"Event location 1" ,"Additional info 1");
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event savedEvent = eventService.createEvent(event);

        assertNotNull(savedEvent);
        verify(eventRepository).save(event);
    }

    @Test
    void testCreateEvent_Failure() {
        Event event = new Event(1L,null,LocalDateTime.now(),"Event location 1" ,"Additional info 1");

        Exception exception = assertThrows(EventValidationException.class, () -> eventService.createEvent(event));

        assertEquals("Event name or date/time or location must not be null", exception.getMessage());
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
