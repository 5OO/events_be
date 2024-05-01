package org.regikeskus.events.service;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.model.Company;
import org.regikeskus.events.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    @Transactional
    public Company createOrUpdateCompany(Company company) {
        if (company.getLegalName() == null || company.getRegistrationCode() == null) {
            throw new IllegalArgumentException("Company must have a registration code and a legal name");
        }
        return companyRepository.save(company);
    }

    @Transactional
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
