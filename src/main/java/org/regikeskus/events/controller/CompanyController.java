package org.regikeskus.events.controller;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.dto.CompanyDTO;
import org.regikeskus.events.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/{participantId}/event/{eventId}")
    public ResponseEntity<CompanyDTO> getCompanyByIdAndEventId(@PathVariable Long participantId, @PathVariable Long eventId) {
        return ResponseEntity.ok(companyService.getCompanyByIdAndEventId(participantId, eventId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable Long id) {
        CompanyDTO company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<CompanyDTO>> getCompaniesByEventId(@PathVariable Long eventId) {
        List<CompanyDTO> companies = companyService.getCompaniesByEventId(eventId);
        if (companies.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companies);
    }

    @PostMapping
    public ResponseEntity<CompanyDTO> createCompany(@RequestBody CompanyDTO companyDTO) {
        CompanyDTO savedCompany = companyService.createCompany(companyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyDTO> updateCompany(@PathVariable Long id, @RequestBody CompanyDTO companyDTO) {
        CompanyDTO updatedCompany = companyService.updateCompany(id, companyDTO);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok().build();
    }
}
