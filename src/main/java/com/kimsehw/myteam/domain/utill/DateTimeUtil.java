package com.kimsehw.myteam.domain.utill;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static final String Y_M_D_H_M_TYPE = "yyyy-MM-dd'T'HH:mm";
    public static final String Y_M_D_TYPE = "yyyy-MM-dd";

    public static LocalDateTime getToDateTime(String toDate, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        if (toDate != null && !toDate.isBlank()) {
            return LocalDate.parse(toDate, formatter).atTime(23, 59, 59);
        }
        return null;
    }

    public static LocalDateTime getFromDateTime(String fromDate, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        if (fromDate != null && !fromDate.isBlank()) {
            return LocalDate.parse(fromDate, formatter).atStartOfDay();
        }
        return null;
    }

    public static LocalDateTime formatting(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        if (date != null && !date.isBlank()) {
            return LocalDateTime.parse(date, formatter);
        }
        return null;
    }
}
