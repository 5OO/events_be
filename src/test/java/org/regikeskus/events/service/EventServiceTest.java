package org.regikeskus.events.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.regikeskus.events.model.Event;
import org.regikeskus.events.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Event> foundEvent = eventService.getEventById(1L);

        assertFalse(foundEvent.isPresent());
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

        Exception exception = assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(event));

        assertEquals("Event name or date/time or location must not be null", exception.getMessage());
    }

    @Test
    void testDeleteEvent() {
        Long id = 1L;

        eventService.deleteEvent(id);

        verify(eventRepository).deleteById(id);
    }

}
