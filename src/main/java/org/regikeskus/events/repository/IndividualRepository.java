package org.regikeskus.events.repository;

import org.regikeskus.events.model.Individual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualRepository extends JpaRepository<Individual, Long> {
}
