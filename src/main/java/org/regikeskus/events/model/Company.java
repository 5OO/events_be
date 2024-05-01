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
    @JoinColumn(name = "EVENT_ID", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String legalName;

    @Column(nullable = false)
    private String registrationCode;

    private Integer numberOfParticipants;
    private String paymentMethod;
    private String additionalInfo;

}
