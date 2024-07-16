package org.example.backend.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormater {

    public static LocalDateTime convertFromString(String str){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDateTime.parse(str, formatter);
    }
}
