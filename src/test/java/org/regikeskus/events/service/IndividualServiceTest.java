package org.regikeskus.events.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.regikeskus.events.model.Event;
import org.regikeskus.events.model.Individual;
import org.regikeskus.events.repository.IndividualRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

}
