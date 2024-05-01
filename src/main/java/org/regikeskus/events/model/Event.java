package org.regikeskus.events.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(nullable = false)
    private String eventName;

    @Column(nullable = false)
    private LocalDateTime eventDateTime;

    @Column(nullable = false)
    private String location;

    private String additionalInfo;
}
