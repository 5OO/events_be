package org.regikeskus.events.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Individuals")
public class Individual {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String personalID;

    private String paymentMethod;
    private String additionalInfo;
}
