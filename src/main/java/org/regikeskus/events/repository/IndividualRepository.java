package org.regikeskus.events.repository;

import org.regikeskus.events.model.Individual;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndividualRepository extends JpaRepository<Individual, Long> {

    List<Individual> findByEventId(Long eventId);

    long countByEventId(Long eventId);

    Optional<Individual>  findByParticipantIdAndEventId(Long participantId, Long eventId);
}
