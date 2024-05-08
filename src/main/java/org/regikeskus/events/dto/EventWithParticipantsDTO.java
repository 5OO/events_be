package org.regikeskus.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventWithParticipantsDTO {
    private Long eventId;
    private String eventName;
    private LocalDate eventDate;
    private int totalParticipants;
}
