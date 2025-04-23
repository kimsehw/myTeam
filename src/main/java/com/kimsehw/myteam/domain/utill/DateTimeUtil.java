package com.kimsehw.myteam.domain.utill;

import com.kimsehw.myteam.constant.search.SearchDateType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    public static final String Y_M_D_H_M_TYPE = "yyyy-MM-dd'T'HH:mm";
    public static final String Y_M_D_TYPE = "yyyy-MM-dd";
    public static final String Y_M_D_H_M_DATE_TYPE = "yyyy-MM-dd HH:mm";

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

    public static String formattingToString(LocalDateTime date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        if (date != null) {
            return date.format(formatter);
        }
        return null;
    }

    public static LocalDateTime getBeforeDateTypeOf(SearchDateType searchDateType) {
        LocalDateTime now = LocalDateTime.now();

        if (SearchDateType.ALL == searchDateType) {
            return now.minusYears(100L);
        }
        if (SearchDateType.TODAY == searchDateType) {
            return now.minusDays(1);
        }
        if (SearchDateType.ONE_WEEK == searchDateType) {
            return now.minusWeeks(1);
        }
        if (SearchDateType.ONE_MONTH == searchDateType) {
            return now.minusMonths(1);
        }
        if (SearchDateType.SIX_MONTH == searchDateType) {
            return now.minusMonths(6);
        }
        if (SearchDateType.ONE_YEAR == searchDateType) {
            return now.minusMonths(12);
        }
        return now;
    }
}
