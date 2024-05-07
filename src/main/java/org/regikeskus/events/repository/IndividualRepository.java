package org.regikeskus.events.repository;

import org.regikeskus.events.model.Individual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IndividualRepository extends JpaRepository<Individual, Long> {
    List<Individual> findByEvent_EventId(Long eventId);
}
