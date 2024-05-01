package org.regikeskus.events.service;

import lombok.RequiredArgsConstructor;
import org.regikeskus.events.model.Individual;
import org.regikeskus.events.repository.IndividualRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IndividualService {
    private final IndividualRepository individualRepository;

    @Transactional(readOnly = true)
    public List<Individual> getAllIndividuals() {
        return individualRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Individual getIndividualById(Long id) {
        return individualRepository.findById(id).orElse(null);
    }

    @Transactional
    public Individual createOrUpdateIndividual(Individual individual) {
        if (individual.getFirstName() == null || individual.getLastName() == null || individual.getPersonalID().isEmpty()) {
            throw new IllegalArgumentException("Individuals must have Name and personal ID");
        }
        return individualRepository.save(individual);
    }

    @Transactional
    public void deleteIndividual(Long id) {
        individualRepository.deleteById(id);
    }
}
