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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}
