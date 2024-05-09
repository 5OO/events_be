package org.regikeskus.events.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.regikeskus.events.dto.CompanyDTO;
import org.regikeskus.events.exception.CompanyNotFoundException;
import org.regikeskus.events.exception.CompanyValidationException;
import org.regikeskus.events.model.Company;
import org.regikeskus.events.repository.CompanyRepository;
import org.regikeskus.events.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private EventRepository eventRepository;

    private List<Company> companies;
    private Company company;

    @BeforeEach
    void setUp() {
        companies = new ArrayList<>();
        companies.add(new Company(1L, 10L, "Weltweit Softwareentwicklung AS", "11629770", 22, "Bank Transfer", "No allergies"));
        companies.add(new Company(2L, 10L, "WeltWeitWeb AS", "16228231", 33, "Cash", "Vegetarian"));
        company = new Company(1L, 10L, "Weltweit Softwareentwicklung AS", "11629770", 22, "Bank Transfer", "No allergies");
    }

    @Test
    void testGetCompanyByIdAndEventId_Exists() {
        Long participantId = 1L;
        Long eventId = 10L;

        when(companyRepository.findByParticipantIdAndEventId(participantId, eventId)).thenReturn(Optional.of(company));

        CompanyDTO foundCompany = companyService.getCompanyByIdAndEventId(participantId, eventId);

        assertNotNull(foundCompany);
        assertEquals("Weltweit Softwareentwicklung AS", foundCompany.getLegalName());
        assertEquals("11629770", foundCompany.getRegistrationCode());
        assertEquals(22, foundCompany.getNumberOfParticipants());
        verify(companyRepository).findByParticipantIdAndEventId(participantId, eventId);
    }

    @Test
    void testGetCompanyByIdAndEventId_NotFound() {
        Long participantId = 1L;
        Long eventId = 10L;

        when(companyRepository.findByParticipantIdAndEventId(participantId, eventId)).thenReturn(Optional.empty());

        CompanyNotFoundException exception = assertThrows(CompanyNotFoundException.class, () -> companyService.getCompanyByIdAndEventId(participantId, eventId));

        assertEquals("Company not found with participant-ID: 1", exception.getMessage());
        verify(companyRepository).findByParticipantIdAndEventId(participantId, eventId);
    }

    @Test
    void testGetCompaniesByEventId() {
        Long eventId = 10L;
        when(companyRepository.findByEventId(eventId)).thenReturn(companies);

        List<CompanyDTO> retrievedCompanies = companyService.getCompaniesByEventId(eventId);

        assertNotNull(retrievedCompanies);
        assertEquals(2, retrievedCompanies.size());
        verify(companyRepository).findByEventId(eventId);
    }

    @Test
    void testGetCompanyById_Exists() {
        Company company = new Company(1L, 10L, "Weltweit Softwareentwicklung AS", "11629770", 22, "Bank Transfer", "No allergies");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        CompanyDTO foundCompany = companyService.getCompanyById(1L);

        assertNotNull(foundCompany);
        assertEquals("Weltweit Softwareentwicklung AS", foundCompany.getLegalName());
        assertEquals("11629770", foundCompany.getRegistrationCode());
        verify(companyRepository).findById(1L);
    }

    @Test
    void testGetCompanyById_NotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        CompanyNotFoundException exception = assertThrows(CompanyNotFoundException.class, () -> companyService.getCompanyById(1L));

        assertEquals("Company not found with participant-ID: 1", exception.getMessage());
        verify(companyRepository).findById(1L);
    }

    @Test
    void testCreateCompany_Success() {
        CompanyDTO companyDTO = new CompanyDTO(1L, 10L, "Weltweit Softwareentwicklung AS", "11629770", 22, "Bank Transfer", "No allergies");
        Company company = new Company(1L, 10L, "Weltweit Softwareentwicklung AS", "11629770", 22, "Bank Transfer", "No allergies");

        when(companyRepository.save(any(Company.class))).thenReturn(company);
        when(eventRepository.existsById(10L)).thenReturn(true);

        CompanyDTO savedCompany = companyService.createCompany(companyDTO);

        assertNotNull(savedCompany);
        assertEquals(companyDTO.getLegalName(), savedCompany.getLegalName());
        verify(companyRepository).save(company);
    }

    @Test
    @DisplayName("Create Company test fails with missing legal name")
    void testCreateCompany_Failure() {
        CompanyDTO companyDTO = new CompanyDTO(1L, 10L, null, "11629770", 22, "Bank Transfer", "No allergies");

        CompanyValidationException exception = assertThrows(CompanyValidationException.class, () -> companyService.createCompany(companyDTO));

        assertEquals("Company must have a registration code and a legal name", exception.getMessage());
    }

    @Test
    void testUpdateCompany_Success() {
        Long participantId = 1L;
        Company existingCompany = new Company(1L, 10L, "Weltweit Softwareentwicklung AS", "11629770", 22, "Bank Transfer", "No allergies");
        CompanyDTO updatedCompanyDTO = new CompanyDTO(1L, 10L, "Weltweit Softwareentwicklung AS", "11629770", 25, "Card", "Updated Allergies");

        when(companyRepository.findById(participantId)).thenReturn(Optional.of(existingCompany));
        when(companyRepository.save(any(Company.class))).thenReturn(existingCompany);
        when(eventRepository.existsById(10L)).thenReturn(true);

        CompanyDTO updatedCompany = companyService.updateCompany(participantId, updatedCompanyDTO);

        assertNotNull(updatedCompany);
        assertEquals(25, updatedCompany.getNumberOfParticipants());
        verify(companyRepository).save(existingCompany);
    }

    @Test
    void testDeleteCompany_Success() {
        Long id = 1L;
        when(companyRepository.existsById(id)).thenReturn(true);

        companyService.deleteCompany(id);

        verify(companyRepository).deleteById(id);
    }

    @Test
    void testDeleteCompany_NotFound() {
        Long id = 1L;
        when(companyRepository.existsById(id)).thenReturn(false);

        CompanyNotFoundException exception = assertThrows(CompanyNotFoundException.class, () -> companyService.deleteCompany(id));

        assertEquals("Company not found with participant-ID: 1", exception.getMessage());
        verify(companyRepository, never()).deleteById(id);
    }
}
