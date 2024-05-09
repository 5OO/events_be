package org.regikeskus.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.regikeskus.events.dto.CompanyDTO;
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
    public CompanyDTO getCompanyById(Long id) {
        return companyRepository.findById(id)
                .map(this::mapToCompanyDTO)
                .orElseThrow(()-> new CompanyNotFoundException(COMPANY_NOT_FOUND_MESSAGE + id));
    }

    @Transactional(readOnly = true)
    public List<CompanyDTO> getCompaniesByEventId(Long eventId) {
        return companyRepository.findByEventId(eventId).stream()
                .map(this::mapToCompanyDTO).toList();
    }

    @Transactional
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        validateCompanyDTO(companyDTO);
        Company company = mapToCompany(companyDTO);
        log.debug("Creating company with event ID {}", company.getEventId());
        Company savedCompany = companyRepository.save(company);
        return mapToCompanyDTO(savedCompany);
    }

    @Transactional
    public CompanyDTO updateCompany(Long participantId, CompanyDTO updatedCompanyDTO) {
        Company company = companyRepository.findById(participantId).orElseThrow(() -> new CompanyNotFoundException(COMPANY_NOT_FOUND_MESSAGE + participantId));

        validateCompanyDTO(updatedCompanyDTO);

        company.setLegalName(updatedCompanyDTO.getLegalName());
        company.setRegistrationCode(updatedCompanyDTO.getRegistrationCode());
        company.setNumberOfParticipants(updatedCompanyDTO.getNumberOfParticipants());
        company.setPaymentMethod(updatedCompanyDTO.getPaymentMethod());
        company.setAdditionalInfo(updatedCompanyDTO.getAdditionalInfo());

        log.debug("Updating company {} with event ID {}", participantId, updatedCompanyDTO.getEventId());
        Company savedCompany = companyRepository.save(company);
        return mapToCompanyDTO(companyRepository.save(savedCompany));
    }

    private void validateCompanyDTO(CompanyDTO companyDTO) {
        if (companyDTO.getLegalName() == null || companyDTO.getRegistrationCode() == null) {
            throw new CompanyValidationException("Company must have a registration code and a legal name");
        }
        if (eventRepository.existsById(companyDTO.getEventId())) {
            throw new EventNotFoundException("Event not found with ID: " + companyDTO.getEventId());
        }
        if (!eventRepository.existsById(companyDTO.getEventId())) {
            throw new EventNotFoundException("Event not found with ID: " + companyDTO.getEventId());
        }
    }

    @Transactional
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new CompanyNotFoundException(COMPANY_NOT_FOUND_MESSAGE + id);
        }
        companyRepository.deleteById(id);
    }

    private CompanyDTO mapToCompanyDTO(Company company) {
        return new CompanyDTO(
                company.getParticipantId(),
                company.getEventId(),
                company.getLegalName(),
                company.getRegistrationCode(),
                company.getNumberOfParticipants(),
                company.getPaymentMethod(),
                company.getAdditionalInfo()
        );
    }

    private Company mapToCompany(CompanyDTO companyDTO) {
        return new Company(
                companyDTO.getParticipantId(),
                companyDTO.getEventId(),
                companyDTO.getLegalName(),
                companyDTO.getRegistrationCode(),
                companyDTO.getNumberOfParticipants(),
                companyDTO.getPaymentMethod(),
                companyDTO.getAdditionalInfo()
        );
    }
}
