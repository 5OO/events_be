package org.regikeskus.events.repository;

import org.regikeskus.events.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
