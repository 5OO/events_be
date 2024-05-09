package org.regikeskus.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualDTO {
    private Long participantId;
    private Long eventId;
    private String firstName;
    private String lastName;
    private String personalID;
    private String paymentMethod;
    private String additionalInfo;
}
