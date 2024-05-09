package org.regikeskus.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventWithAggregatedParticipantsDTO {
    private Long eventId;
    private String eventName;
    private LocalDate eventDate;
    private String location;
    private List<AggregatedParticipantDTO> participants;
}