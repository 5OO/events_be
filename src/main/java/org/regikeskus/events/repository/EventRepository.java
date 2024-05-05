package org.regikeskus.events.repository;

import org.regikeskus.events.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE e.eventDateTime >= :now ORDER BY e.eventDateTime ASC")
    List<Event> findAllFutureOrCurrentEvents(LocalDateTime now);

    @Query("SELECT e FROM Event e WHERE e.eventDateTime < :now ORDER BY e.eventDateTime DESC")
    List<Event> findAllPastEvents(LocalDateTime now);

}
