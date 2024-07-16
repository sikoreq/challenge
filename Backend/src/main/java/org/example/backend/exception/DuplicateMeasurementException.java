package org.example.backend.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DuplicateMeasurementException extends RuntimeException{

    public DuplicateMeasurementException(String city, LocalDateTime dateTime){
        super("Duplicate measurement for city "+city+" at "+dateTime);
    }

}
