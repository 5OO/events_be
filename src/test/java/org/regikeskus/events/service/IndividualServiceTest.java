package org.regikeskus.events.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.regikeskus.events.model.Event;
import org.regikeskus.events.model.Individual;
import org.regikeskus.events.repository.IndividualRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IndividualServiceTest {

    @InjectMocks
    private IndividualService individualService;

    @Mock
    private IndividualRepository individualRepository;

    @Test
    void testGetAllIndividuals() {
        List<Individual> individuals = new ArrayList<>();
        individuals.add(new Individual(1L, null, "Kalju", "Saar", "12345678901", "Bank Transfer", "No allergies"));
        individuals.add(new Individual(2L, null, "Kati", "Saar", "23456789012", "Cash", "Vegetarian"));
        when(individualRepository.findAll()).thenReturn(individuals);

        List<Individual> retrievedIndividuals = individualService.getAllIndividuals();

        assertNotNull(retrievedIndividuals);
        assertEquals(2, retrievedIndividuals.size());
        assertEquals(individuals, retrievedIndividuals);
        verify(individualRepository).findAll();
    }

    @Test
    void testGetIndividualById_Exists() {
        Optional<Individual> individual = Optional.of(new Individual(1L, null, "Kalju", "Saar", "12345678901", "Bank Transfer", "No allergies"));
        when(individualRepository.findById(1L)).thenReturn(individual);

        Optional<Individual> foundIndividual = individualService.getIndividualById(1L);

        assertTrue(foundIndividual.isPresent());
        assertEquals("Kalju", foundIndividual.get().getFirstName());
        assertEquals("12345678901", foundIndividual.get().getPersonalID());
        verify(individualRepository).findById(1L);
    }

    @Test
    void testGetIndividualById_NotFound() {
        when(individualRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Individual> foundIndividual = individualService.getIndividualById(1L);

        assertFalse(foundIndividual.isPresent());
        verify(individualRepository).findById(1L);
    }

    @Test
    void testCreateOrUpdateIndividual_Success() {
        Individual individual = new Individual(1L, null, "Kalju", "Saar", "51107121760", "Bank Transfer", "No allergies");
        when(individualRepository.save(any(Individual.class))).thenReturn(individual);

        Individual savedIndividual = individualService.createOrUpdateIndividual(individual);

        assertNotNull(savedIndividual);
        verify(individualRepository).save(individual);
    }

    @Test
    @DisplayName("createOrUpdateIndividual test fails with missing Name")
    void testCreateOrUpdateIndividual_FailureMissingName() {
        Individual individual = new Individual(1L, null, null, "Saar", "51107121760", "Bank Transfer", "No allergies");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> individualService.createOrUpdateIndividual(individual));

        assertEquals("Individuals must have Name", exception.getMessage());

    }

}
