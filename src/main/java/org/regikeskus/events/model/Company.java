package org.regikeskus.events.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    private Event event;

    private String legalName;
    private String registrationCode;
    private Integer numberOfParticipants;
    private String paymentMethod;
    private String additionalInfo;

}
