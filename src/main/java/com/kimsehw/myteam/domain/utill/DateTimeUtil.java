package com.kimsehw.myteam.domain.utill;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static LocalDateTime getToDateTime(String toDate, DateTimeFormatter formatter) {
        if (toDate != null && !toDate.isBlank()) {
            return LocalDate.parse(toDate, formatter).atTime(23, 59, 59);
        }
        return null;
    }

    public static LocalDateTime getFromDateTime(String fromDate, DateTimeFormatter formatter) {
        if (fromDate != null && !fromDate.isBlank()) {
            return LocalDate.parse(fromDate, formatter).atStartOfDay();
        }
        return null;
    }
}
