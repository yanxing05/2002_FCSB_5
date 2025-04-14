package bto.system.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
    }
    public static LocalDateTime convertToLocalDateTime(String rawDateStr) {
    	rawDateStr = rawDateStr.trim();
        try {
            // Check if the string contains a timezone (e.g., GMT, +08:00, etc.)
            if (rawDateStr.matches(".*[A-Za-z]{3,}|.*\\+\\d{2}:\\d{2}.*")) {
                DateTimeFormatter zonedFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(rawDateStr, zonedFormatter);
                return zonedDateTime.toLocalDateTime();
            } else {
                DateTimeFormatter standardFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                return LocalDateTime.parse(rawDateStr, standardFormatter);
            }
        } catch (Exception e) {
            System.err.println("Failed to parse date string: " + rawDateStr);
            e.printStackTrace();
            return null;
        }
    }
}
