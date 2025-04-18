package bto.system.exceptions;

/**
 * Thrown when authentication fails due to invalid credentials,
 * locked accounts, or other security-related issues
 */
public class AuthenticationException extends Exception {
    private final String nric;
    private final String errorCode;

    public AuthenticationException(String message) {
        super(message);
        this.nric = null;
        this.errorCode = "AUTH_001";
    }

    public AuthenticationException(String message, String nric) {
        super(message);
        this.nric = nric;
        this.errorCode = "AUTH_002";
    }

    public AuthenticationException(String message, String nric, String errorCode) {
        super(message);
        this.nric = nric;
        this.errorCode = errorCode;
    }

    // Getters
    public String getNric() {
        return nric;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        String base = "Authentication Error [" + errorCode + "]: " + getMessage();
        return nric != null ? base + " (NRIC: " + nric + ")" : base;
    }
}