//package bto.system.utils;
//
//import bto.system.exceptions.InvalidInputException;
//
//public class InputValidator {
//    public static void validateNRIC(String nric) throws InvalidInputException {
//        if (!nric.matches("[ST]\\d{7}[A-Z]")) {
//            throw new InvalidInputException("Invalid NRIC format");
//        }
//    }
//}

package bto.system.utils;

import bto.system.exceptions.InvalidInputException;
import java.time.LocalDate;

public class InputValidator {
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }

    public static void validateProjectDates(LocalDate opening, LocalDate closing)
            throws InvalidInputException {
        if (closing.isBefore(opening)) {
            throw new InvalidInputException("Closing date must be after opening date");
        }
    }
}