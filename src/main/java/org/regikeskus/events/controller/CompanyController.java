package org.regikeskus.events.controller;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.model.Company;
import org.regikeskus.events.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Optional<Company> company = companyService.getCompanyById(id);
        return company.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company savedCompany = companyService.createOrUpdateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        return companyService.getCompanyById(id).map(savedCompany -> {
            savedCompany.setLegalName(company.getLegalName());
            savedCompany.setRegistrationCode(company.getRegistrationCode());
            savedCompany.setNumberOfParticipants(company.getNumberOfParticipants());
            savedCompany.setPaymentMethod(company.getPaymentMethod());
            savedCompany.setAdditionalInfo(company.getAdditionalInfo());
            Company updatedCompany = companyService.createOrUpdateCompany(company);
            return ResponseEntity.ok(updatedCompany);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCompany(@PathVariable Long id) {
        return companyService.getCompanyById(id).map(_ -> {
            companyService.deleteCompany(id);
            return ResponseEntity.ok().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
