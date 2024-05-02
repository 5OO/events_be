package org.regikeskus.events.service;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.model.Individual;
import org.regikeskus.events.repository.IndividualRepository;
import org.regikeskus.events.validation.IdValidationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IndividualService {

    private final IndividualRepository individualRepository;

    @Transactional(readOnly = true)
    public List<Individual> getAllIndividuals() {
        return individualRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Individual> getIndividualById(Long id) {
        return individualRepository.findById(id);
    }

    @Transactional
    public Individual createOrUpdateIndividual(Individual individual) {
        if (individual.getFirstName() == null || individual.getLastName() == null ) {
            throw new IllegalArgumentException("Individuals must have Name");
        }
        if (!IdValidationUtils.isValidEstonianPersonalId(individual.getPersonalID())) {
            throw new IllegalArgumentException("Invalid Estonian Personal ID.");
        }
        return individualRepository.save(individual);
    }

    @Transactional
    public void deleteIndividual(Long id) {
        individualRepository.deleteById(id);
    }
}
