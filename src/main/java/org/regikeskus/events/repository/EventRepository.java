package org.regikeskus.events.repository;

import org.regikeskus.events.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
