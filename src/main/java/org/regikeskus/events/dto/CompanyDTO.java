package org.regikeskus.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private Long participantId;
    private Long eventId;
    private String legalName;
    private String registrationCode;
    private Integer numberOfParticipants;
    private String paymentMethod;
    private String additionalInfo;
}
