package bto.system.models;

import java.time.LocalDate;

public class Report {
    private String reportId;
    private String title;
    private String content;
    private LocalDate generationDate;
    private String generatedBy;

    public Report(String title, String content) {
        this.reportId = "REP-" + System.currentTimeMillis();
        this.title = title;
        this.content = content;
        this.generationDate = LocalDate.now();
    }

    // Getters and setters
    public String getReportId() { return reportId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDate getGenerationDate() { return generationDate; }
    public String getGeneratedBy() { return generatedBy; }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    @Override
    public String toString() {
        return title + " - " + generationDate;
    }
}