package org.regikeskus.events.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @Column(name = "EVENT_ID", nullable = false)
    private Long eventId;

    @Column(nullable = false)
    private String legalName;

    @Column(nullable = false)
    private String registrationCode;

    @Column(nullable = false)
    private Integer numberOfParticipants;
    private String paymentMethod;
    private String additionalInfo;

}
