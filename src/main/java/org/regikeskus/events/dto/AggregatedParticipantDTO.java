package org.regikeskus.events.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedParticipantDTO {
    private Long participantId;
    private String participantType;
    private String name;
    private String codeOrId;
}