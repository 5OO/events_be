package org.regikeskus.events.service;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.exception.CompanyNotFoundException;
import org.regikeskus.events.exception.CompanyValidationException;
import org.regikeskus.events.model.Company;
import org.regikeskus.events.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CompanyService {

    private static final String COMPANY_NOT_FOUND_MESSAGE = "Company not found with participant-ID: ";

    private final CompanyRepository companyRepository;

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
        return companyRepository.findByEvent_EventId(eventId);
    }

    @Transactional
    public Company createCompany(Company company) {
        validateCompany(company);
        return companyRepository.save(company);
    }

    @Transactional
    public Company updateCompany(Long participantId, Company updatedCompany) {
        Company company = companyRepository.findById(participantId).orElseThrow(() -> new CompanyNotFoundException(COMPANY_NOT_FOUND_MESSAGE + participantId));

        company.setLegalName(updatedCompany.getLegalName());
        company.setRegistrationCode(updatedCompany.getRegistrationCode());
        company.setNumberOfParticipants(updatedCompany.getNumberOfParticipants());
        company.setPaymentMethod(updatedCompany.getPaymentMethod());
        company.setAdditionalInfo(updatedCompany.getAdditionalInfo());

        validateCompany(company);
        return companyRepository.save(company);
    }

    private void validateCompany(Company company) {
        if (company.getLegalName() == null || company.getRegistrationCode() == null) {
            throw new CompanyValidationException("Company must have a registration code and a legal name");
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
