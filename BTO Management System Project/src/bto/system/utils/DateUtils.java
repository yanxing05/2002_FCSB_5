package bto.system.utils;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateUtils {
    private static final DateTimeFormatter EXCEL_FORMAT =
        DateTimeFormatter.ofPattern("d/M/yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter LONG_FORMAT =
        DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
    private static final DateTimeFormatter CREATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

    public static LocalDate parseDate(String dateStr) {
        dateStr = dateStr.trim();

        // Try Excel style format first
        try {
            return LocalDate.parse(dateStr, EXCEL_FORMAT);
        } catch (DateTimeParseException ignored) {}

        // Try long format (e.g. "Sat Feb 15 00:00:00 GMT+08:00 2025")
        try {
            return ZonedDateTime.parse(dateStr, LONG_FORMAT).toLocalDate();
        } catch (DateTimeParseException ignored) {}

        // Add more formats if needed...
        try {
            return ZonedDateTime.parse(dateStr, CREATE_FORMAT).toLocalDate();
        } catch (DateTimeParseException ignored) {}

        // If nothing matches, throw clear exception
        throw new IllegalArgumentException("Unsupported date format: " + dateStr);
    }
}

