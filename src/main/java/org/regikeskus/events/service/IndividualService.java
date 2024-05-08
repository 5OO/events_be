package org.regikeskus.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.regikeskus.events.exception.EventNotFoundException;
import org.regikeskus.events.exception.IndividualNotFoundException;
import org.regikeskus.events.exception.IndividualValidationException;
import org.regikeskus.events.model.Event;
import org.regikeskus.events.model.Individual;
import org.regikeskus.events.repository.EventRepository;
import org.regikeskus.events.repository.IndividualRepository;
import org.regikeskus.events.validation.IdValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class IndividualService {

    private static final String INDIVIDUAL_NOT_FOUND_MESSAGE = "Individual not found with participant-ID: ";

    private final EventRepository eventRepository;
    private final IndividualRepository individualRepository;

    @Transactional(readOnly = true)
    public List<Individual> getAllIndividuals() {
        return individualRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Individual getIndividualById(Long id) {
        log.debug("Retrieving Individuals By participantId {}", id);
        return individualRepository.findById(id)
                .orElseThrow(() -> new IndividualNotFoundException(INDIVIDUAL_NOT_FOUND_MESSAGE + id));
    }

    @Transactional(readOnly = true)
    public List<Individual> getIndividualsByEventId(Long eventId) {
        log.debug("Retrieving Individuals By EventId {}", eventId);
        return individualRepository.findByEventId(eventId);
    }

    @Transactional
    public Individual createIndividual(Individual individual) {
        validateIndividual(individual);
        log.debug("Creating individual with event ID {}", individual.getEventId());
        return individualRepository.save(individual);
    }

    @Transactional
    public Individual updateIndividual(Long participantID, Individual updatedIndividual) {
        Individual individual = individualRepository.findById(participantID)
                .orElseThrow(() -> new IndividualNotFoundException(INDIVIDUAL_NOT_FOUND_MESSAGE + participantID));

        validateIndividual(updatedIndividual);

        individual.setFirstName(updatedIndividual.getFirstName());
        individual.setLastName(updatedIndividual.getLastName());
        individual.setPersonalID(updatedIndividual.getPersonalID());
        individual.setPaymentMethod(updatedIndividual.getPaymentMethod());
        individual.setAdditionalInfo(updatedIndividual.getAdditionalInfo());

        log.debug("Updating individual {} with event ID {}", participantID, individual.getEventId());
        return individualRepository.save(individual);
    }

    private void validateIndividual(Individual individual) {
        if (individual.getFirstName() == null || individual.getLastName() == null) {
            throw new IndividualValidationException("First name and last name must not be null.");
        }
        if (!IdValidationUtils.isValidEstonianPersonalId(individual.getPersonalID())) {
            throw new IndividualValidationException("Invalid Estonian Personal ID. " + individual.getPersonalID());
        }
        if (!eventRepository.existsById(individual.getEventId())) {
            throw new EventNotFoundException("Event not found with ID: " + individual.getEventId());
        }
    }

    @Transactional
    public void deleteIndividual(Long id) {
        if (!individualRepository.existsById(id)) {
            throw new IndividualNotFoundException(INDIVIDUAL_NOT_FOUND_MESSAGE + id);
        }
        log.debug("Deleting individual {}", id);
        individualRepository.deleteById(id); }
}
