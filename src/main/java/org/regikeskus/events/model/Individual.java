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

    private String firstName;
    private String lastName;
    private String personalID;
    private String paymentMethod;
    private String additionalInfo;
}
