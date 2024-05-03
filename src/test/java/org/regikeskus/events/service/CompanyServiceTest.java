package org.regikeskus.events.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.regikeskus.events.model.Company;
import org.regikeskus.events.repository.CompanyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    void testGetAllCompanies() {
        List<Company> companies = new ArrayList<>();
        companies.add(new Company(1L, null, "Weltweit Softwareentwicklung AS", "11629770", 22, "Bank Transfer", "No allergies"));
        companies.add(new Company(2L, null, "WeltWeitWeb AS", "16228231", 33, "Cash", "Vegetarian"));
        when(companyRepository.findAll()).thenReturn(companies);

        List<Company> retrievedCompanies = companyService.getAllCompanies();

        assertNotNull(retrievedCompanies);
        assertEquals(2, retrievedCompanies.size());
        assertEquals(companies, retrievedCompanies);
        verify(companyRepository).findAll();
    }

    @Test
    void testGetCompanyById_Exists() {
        Optional<Company> company = Optional.of(new Company(1L, null, "Weltweit Softwareentwicklung AS", "11629770", 22, "Bank Transfer", "No allergies"));
        when(companyRepository.findById(1L)).thenReturn(company);

        Optional<Company> foundCompany = companyService.getCompanyById(1L);

        assertTrue(foundCompany.isPresent());
        assertEquals("Weltweit Softwareentwicklung AS", foundCompany.get().getLegalName());
        assertEquals("11629770", foundCompany.get().getRegistrationCode());
        verify(companyRepository).findById(1L);
    }

    @Test
    void testGetCompanyById_NotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Company> foundCompany = companyService.getCompanyById(1L);

        assertFalse(foundCompany.isPresent());
        verify(companyRepository).findById(1L);
    }

    @Test
    void testCreateOrUpdateCompany_Success() {
        Company company = new Company(1L, null, "Weltweit Softwareentwicklung AS", "11629770", 22, "Bank Transfer", "No allergies");
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        Company savedCompany = companyService.createOrUpdateCompany(company);

        assertNotNull(savedCompany);
        verify(companyRepository).save(company);
    }

    @Test
    @DisplayName("createOrUpdateIndividual test with missing legalName")
    void testCreateOrUpdateCompany_Failure() {
        Company company = new Company(1L, null, null, "11629770", 22, "Bank Transfer", "No allergies");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> companyService.createOrUpdateCompany(company));

        assertEquals("Company must have a registration code and a legal name", exception.getMessage());

    }

    @Test
    @DisplayName("DeleteIndividual test")
    void testDeleteIndividual() {
        Long id = 1L;

        companyService.deleteCompany(id);

        Mockito.verify(companyRepository, Mockito.times(1)).deleteById(id);
    }
}
