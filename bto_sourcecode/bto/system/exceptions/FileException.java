package bto.system.exceptions;

import java.io.File;

/**
 * Thrown for file-related operations (reading/writing Excel files)
 */
public class FileException extends Exception {
    private final File problematicFile;
    private final String operationType;

    public FileException(String message) {
        super(message);
        this.problematicFile = null;
        this.operationType = "UNKNOWN";
    }

    public FileException(String message, File file) {
        super(message);
        this.problematicFile = file;
        this.operationType = "IO_OPERATION";
    }

    public FileException(String message, String operationType, Throwable cause) {
        super(message, cause);
        this.problematicFile = null;
        this.operationType = operationType;
    }

    // Getters
    public File getProblematicFile() {
        return problematicFile;
    }

    public String getOperationType() {
        return operationType;
    }

    @Override
    public String toString() {
        String base = "File Error during " + operationType + ": " + getMessage();
        if (problematicFile != null) {
            base += "\nFile: " + problematicFile.getAbsolutePath();
        }
        if (getCause() != null) {
            base += "\nCaused by: " + getCause().getMessage();
        }
        return base;
    }
}