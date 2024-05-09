package org.regikeskus.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.regikeskus.events.dto.IndividualDTO;
import org.regikeskus.events.exception.EventNotFoundException;
import org.regikeskus.events.exception.IndividualNotFoundException;
import org.regikeskus.events.exception.IndividualValidationException;
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
    public IndividualDTO getIndividualById(Long id) {
        log.debug("Retrieving Individuals By participantId {}", id);
        return individualRepository.findById(id)
                .map(this::mapToIndividualDTO)
                .orElseThrow(() -> new IndividualNotFoundException(INDIVIDUAL_NOT_FOUND_MESSAGE + id));
    }

    @Transactional(readOnly = true)
    public List<IndividualDTO> getIndividualsByEventId(Long eventId) {
        log.debug("Retrieving Individuals By EventId {}", eventId);
        return individualRepository.findByEventId(eventId).stream()
                .map(this::mapToIndividualDTO).toList();
    }

    @Transactional
    public IndividualDTO createIndividual(IndividualDTO individualDTO) {
        validateIndividualDTO(individualDTO);
        Individual individual = mapToIndividual(individualDTO);
        log.debug("Creating individual with event ID {}", individualDTO.getEventId());
         Individual savedIndividual = individualRepository.save(individual);
        return mapToIndividualDTO(savedIndividual);
    }

    @Transactional
    public IndividualDTO updateIndividual(Long participantID, IndividualDTO individualDTO) {
        Individual individual = individualRepository.findById(participantID)
                .orElseThrow(() -> new IndividualNotFoundException(INDIVIDUAL_NOT_FOUND_MESSAGE + participantID));

        validateIndividualDTO(individualDTO);

        individual.setFirstName(individualDTO.getFirstName());
        individual.setLastName(individualDTO.getLastName());
        individual.setPersonalID(individualDTO.getPersonalID());
        individual.setPaymentMethod(individualDTO.getPaymentMethod());
        individual.setAdditionalInfo(individualDTO.getAdditionalInfo());

        log.debug("Updating individual {} with event ID {}", participantID, individual.getEventId());
        Individual updatedIndividual =  individualRepository.save(individual);
        return mapToIndividualDTO(updatedIndividual);
    }

    private void validateIndividualDTO(IndividualDTO individualDTO) {
        if (individualDTO.getFirstName() == null || individualDTO.getLastName() == null) {
            throw new IndividualValidationException("First name and last name must not be null.");
        }
        if (!IdValidationUtils.isValidEstonianPersonalId(individualDTO.getPersonalID())) {
            throw new IndividualValidationException("Invalid Estonian Personal ID. " + individualDTO.getPersonalID());
        }
        if (!eventRepository.existsById(individualDTO.getEventId())) {
            throw new EventNotFoundException("Event not found with ID: " + individualDTO.getEventId());
        }
    }

    @Transactional
    public void deleteIndividual(Long id) {
        if (!individualRepository.existsById(id)) {
            throw new IndividualNotFoundException(INDIVIDUAL_NOT_FOUND_MESSAGE + id);
        }
        log.debug("Deleting individual {}", id);
        individualRepository.deleteById(id); }

    private IndividualDTO mapToIndividualDTO(Individual individual) {
        return new IndividualDTO(
                individual.getParticipantId(),
                individual.getEventId(),
                individual.getFirstName(),
                individual.getLastName(),
                individual.getPersonalID(),
                individual.getPaymentMethod(),
                individual.getAdditionalInfo()
        );
    }

    private Individual mapToIndividual(IndividualDTO individualDTO) {
        return new Individual(
                individualDTO.getParticipantId(),
                individualDTO.getEventId(),
                individualDTO.getFirstName(),
                individualDTO.getLastName(),
                individualDTO.getPersonalID(),
                individualDTO.getPaymentMethod(),
                individualDTO.getAdditionalInfo()
        );
    }
}
