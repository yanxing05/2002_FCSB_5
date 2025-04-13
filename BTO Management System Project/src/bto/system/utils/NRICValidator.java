package bto.system.utils;

public class NRICValidator {
    public static boolean isValidNRIC(String nric) {
        return nric != null &&
                nric.matches("[STFG]\\d{7}[A-Z]") &&
                nric.length() == 9;
    }
}
