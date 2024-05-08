package org.regikeskus.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Long eventId;
    private String eventName;
    private LocalDateTime eventDateTime;
    private String location;
    private String additionalInfo;
}
