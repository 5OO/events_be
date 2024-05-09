package org.regikeskus.events.repository;

import org.regikeskus.events.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findByEventId(Long eventId);

    @Query("SELECT SUM(c.numberOfParticipants) FROM Company c WHERE c.eventId = :eventId")
    Long sumNumberOfParticipantsByEventId(Long eventId);

    Optional<Company> findByParticipantIdAndEventId(Long participantId, Long eventId);
}
