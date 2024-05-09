package org.regikeskus.events.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.regikeskus.events.dto.IndividualDTO;
import org.regikeskus.events.exception.IndividualNotFoundException;
import org.regikeskus.events.exception.IndividualValidationException;
import org.regikeskus.events.model.Individual;
import org.regikeskus.events.repository.EventRepository;
import org.regikeskus.events.repository.IndividualRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndividualServiceTest {

    @InjectMocks
    private IndividualService individualService;

    @Mock
    private IndividualRepository individualRepository;


    @Mock
    private EventRepository eventRepository;
    private Individual individual;
    private List<Individual> individuals;

    @BeforeEach
    void setUp() {
        individuals = new ArrayList<>();
        individuals.add(new Individual(1L, 10L, "Kalju", "Saar", "51107121760", "Bank Transfer", "No allergies"));
        individuals.add(new Individual(2L, 10L, "Kati", "Saar", "60010123456", "Cash", "Vegetarian"));

        individual = new Individual(1L, 10L, "Kalju", "Saar", "51107121760", "Bank Transfer", "No allergies");
    }


    @Test
    void testGetIndividualByIdAndEventId_Exists() {
        Long participantId = 1L;
        Long eventId = 10L;

        when(individualRepository.findByParticipantIdAndEventId(participantId, eventId)).thenReturn(Optional.of(individual));

        IndividualDTO foundIndividual = individualService.getIndividualByIdAndEventId(participantId, eventId);

        assertNotNull(foundIndividual);
        assertEquals("Kalju", foundIndividual.getFirstName());
        assertEquals("Saar", foundIndividual.getLastName());
        assertEquals("51107121760", foundIndividual.getPersonalID());
        assertEquals("Bank Transfer", foundIndividual.getPaymentMethod());
        verify(individualRepository).findByParticipantIdAndEventId(participantId, eventId);
    }

    @Test
    void testGetIndividualByIdAndEventId_NotFound() {
        Long participantId = 1L;
        Long eventId = 10L;

        when(individualRepository.findByParticipantIdAndEventId(participantId, eventId)).thenReturn(Optional.empty());

        IndividualNotFoundException exception = assertThrows(IndividualNotFoundException.class, () -> individualService.getIndividualByIdAndEventId(participantId, eventId));

        assertEquals("Individual not found with participant-ID: " + participantId, exception.getMessage());
        verify(individualRepository).findByParticipantIdAndEventId(participantId, eventId);
    }

    @Test
    void testGetIndividualsByEventId() {
        Long eventId = 10L;
        when(individualRepository.findByEventId(eventId)).thenReturn(individuals);

        List<IndividualDTO> retrievedIndividuals = individualService.getIndividualsByEventId(eventId);

        assertNotNull(retrievedIndividuals);
        assertEquals(2, retrievedIndividuals.size());
        verify(individualRepository).findByEventId(eventId);
    }

    @Test
    void testGetIndividualById_Exists() {
        Individual individual = new Individual(1L, 10L, "Kalju", "Saar", "51107121760", "Bank Transfer", "No allergies");
        when(individualRepository.findById(1L)).thenReturn(Optional.of(individual));

        IndividualDTO foundIndividual = individualService.getIndividualById(1L);

        assertNotNull(foundIndividual);
        assertEquals("Kalju", foundIndividual.getFirstName());
        assertEquals("51107121760", foundIndividual.getPersonalID());
        verify(individualRepository).findById(1L);
    }

    @Test
    void testGetIndividualById_NotFound() {
        when(individualRepository.findById(1L)).thenReturn(Optional.empty());

        IndividualNotFoundException exception = assertThrows(IndividualNotFoundException.class, () -> individualService.getIndividualById(1L));

        assertEquals("Individual not found with participant-ID: 1", exception.getMessage());
        verify(individualRepository).findById(1L);
    }

    @Test
    void testCreateIndividual_Success() {
        IndividualDTO individualDTO = new IndividualDTO(1L, 10L, "Kalju", "Saar", "51107121760", "Bank Transfer", "No allergies");
        Individual individual = new Individual(1L, 10L, "Kalju", "Saar", "51107121760", "Bank Transfer", "No allergies");

        when(individualRepository.save(any(Individual.class))).thenReturn(individual);
        when(eventRepository.existsById(10L)).thenReturn(true);

        IndividualDTO savedIndividual = individualService.createIndividual(individualDTO);

        assertNotNull(savedIndividual);
        assertEquals(individualDTO.getFirstName(), savedIndividual.getFirstName());
        verify(individualRepository).save(individual);
    }

    @Test
    @DisplayName("createOrUpdateIndividual test fails with missing name")
    void testCreateIndividual_FailureMissingName() {
        IndividualDTO individualDTO = new IndividualDTO(1L, 10L, null, "Saar", "51107121760", "Bank Transfer", "No allergies");

        Exception exception = assertThrows(IndividualValidationException.class, () -> individualService.createIndividual(individualDTO));

        assertEquals("First name and last name must not be null.", exception.getMessage());
    }

    @Test
    @DisplayName("createOrUpdateIndividual test fails with invalid Personal ID")
    void testCreateIndividual_FailureInvalidPersonalId() {
        IndividualDTO individualDTO = new IndividualDTO(1L, 10L, "Kalju", "Saar", "12345678901", "Bank Transfer", "No allergies");

        Exception exception = assertThrows(IndividualValidationException.class, () -> individualService.createIndividual(individualDTO));

        assertEquals("Invalid Estonian Personal ID. 12345678901", exception.getMessage());
    }

    @Test
    void testUpdateIndividual_Success() {
        Long participantId = 1L;
        Individual existingIndividual = new Individual(1L, 10L, "Kalju", "Saar", "51107121760", "Bank Transfer", "No allergies");
        IndividualDTO updatedIndividualDTO = new IndividualDTO(1L, 10L, "Kalju", "Saar", "51107121760", "Card", "Vegetarian");

        when(individualRepository.findById(participantId)).thenReturn(Optional.of(existingIndividual));
        when(individualRepository.save(any(Individual.class))).thenReturn(existingIndividual);
        when(eventRepository.existsById(10L)).thenReturn(true);

        IndividualDTO updatedIndividual = individualService.updateIndividual(participantId, updatedIndividualDTO);

        assertNotNull(updatedIndividual);
        assertEquals("Card", updatedIndividual.getPaymentMethod());
        verify(individualRepository).save(existingIndividual);
    }

    @Test
    @DisplayName("Delete individual test")
    void testDeleteIndividual() {
        Long id = 1L;
        when(individualRepository.existsById(id)).thenReturn(true);

        individualService.deleteIndividual(id);

        verify(individualRepository).deleteById(id);
    }

    @Test
    @DisplayName("Delete individual not found test")
    void testDeleteIndividual_NotFound() {
        Long id = 1L;
        when(individualRepository.existsById(id)).thenReturn(false);

        IndividualNotFoundException exception = assertThrows(IndividualNotFoundException.class, () -> individualService.deleteIndividual(id));

        assertEquals("Individual not found with participant-ID: 1", exception.getMessage());
        verify(individualRepository, never()).deleteById(id);
    }
}
