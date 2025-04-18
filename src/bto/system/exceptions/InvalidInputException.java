package bto.system.exceptions;

/**
 * Thrown when user input fails validation checks
 */
public class InvalidInputException extends Exception {
    private final String fieldName;
    private final String inputValue;
    private final String validationRule;

    public InvalidInputException(String message) {
        super(message);
        this.fieldName = "UNSPECIFIED";
        this.inputValue = null;
        this.validationRule = "GENERIC_VALIDATION";
    }

    public InvalidInputException(String message, String fieldName, String inputValue) {
        super(message);
        this.fieldName = fieldName;
        this.inputValue = inputValue;
        this.validationRule = "FIELD_VALIDATION";
    }

    public InvalidInputException(String message, String fieldName,
                                 String inputValue, String validationRule) {
        super(message);
        this.fieldName = fieldName;
        this.inputValue = inputValue;
        this.validationRule = validationRule;
    }

    // Getters
    public String getFieldName() {
        return fieldName;
    }

    public String getInputValue() {
        return inputValue;
    }

    public String getValidationRule() {
        return validationRule;
    }

    @Override
    public String toString() {
        return String.format(
                "Input Validation Failed [%s]\nField: %s\nInvalid Value: %s\nRule: %s",
                getMessage(),
                fieldName,
                inputValue != null ? inputValue : "<null>",
                validationRule
        );
    }
}
