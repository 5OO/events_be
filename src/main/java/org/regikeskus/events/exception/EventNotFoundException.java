package org.regikeskus.events.exception;

public class EventNotFoundException extends RuntimeException{
    public EventNotFoundException(String message) {
        super(message);
    }
}
