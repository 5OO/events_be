package org.regikeskus.events.repository;

import org.regikeskus.events.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByEvent_EventId(Long eventId);
}
