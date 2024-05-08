package org.regikeskus.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.regikeskus.events.exception.CompanyNotFoundException;
import org.regikeskus.events.exception.CompanyValidationException;
import org.regikeskus.events.exception.EventNotFoundException;
import org.regikeskus.events.model.Company;
import org.regikeskus.events.repository.CompanyRepository;
import org.regikeskus.events.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyService {

    private static final String COMPANY_NOT_FOUND_MESSAGE = "Company not found with participant-ID: ";

    private final CompanyRepository companyRepository;
    private final EventRepository eventRepository;

    @Transactional(readOnly = true)
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(()-> new CompanyNotFoundException(COMPANY_NOT_FOUND_MESSAGE + id));
    }

    @Transactional(readOnly = true)
    public List<Company> getCompaniesByEventId(Long eventId) {
        return companyRepository.findByEventId(eventId);
    }

    @Transactional
    public Company createCompany(Company company) {
        validateCompany(company);
        log.debug("Creating company with event ID {}", company.getEventId());
        return companyRepository.save(company);
    }

    @Transactional
    public Company updateCompany(Long participantId, Company updatedCompany) {
        Company company = companyRepository.findById(participantId).orElseThrow(() -> new CompanyNotFoundException(COMPANY_NOT_FOUND_MESSAGE + participantId));

        validateCompany(updatedCompany);

        company.setLegalName(updatedCompany.getLegalName());
        company.setRegistrationCode(updatedCompany.getRegistrationCode());
        company.setNumberOfParticipants(updatedCompany.getNumberOfParticipants());
        company.setPaymentMethod(updatedCompany.getPaymentMethod());
        company.setAdditionalInfo(updatedCompany.getAdditionalInfo());

        log.debug("Updating company {} with event ID {}", participantId, updatedCompany.getEventId());
        return companyRepository.save(company);
    }

    private void validateCompany(Company company) {
        if (company.getLegalName() == null || company.getRegistrationCode() == null) {
            throw new CompanyValidationException("Company must have a registration code and a legal name");
        }
        if (eventRepository.existsById(company.getEventId())) {
            throw new EventNotFoundException("Event not found with ID: " + company.getEventId());
        }
        if (!eventRepository.existsById(company.getEventId())) {
            throw new EventNotFoundException("Event not found with ID: " + company.getEventId());
        }
    }

    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new CompanyNotFoundException(COMPANY_NOT_FOUND_MESSAGE + id);
        }
        companyRepository.deleteById(id);
    }
}
