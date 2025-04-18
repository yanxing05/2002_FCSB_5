package bto.system.utils;

import bto.system.models.BTOProject;
import bto.system.models.users.User;
import java.time.LocalDate;
import java.util.UUID;

public class Receipt {
    private String receiptId;
    private User user;
    private BTOProject project;
    private String flatType;
    private double amountPaid;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String referenceNumber;

    public Receipt(User user, BTOProject project, String flatType, double amountPaid) {
        this.receiptId = "RCPT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.user = user;
        this.project = project;
        this.flatType = flatType;
        this.amountPaid = amountPaid;
        this.paymentDate = LocalDate.now();
        this.referenceNumber = generateReferenceNumber();
    }

    // Getters
    public String getReceiptId() { return receiptId; }
    public User getUser() { return user; }
    public BTOProject getProject() { return project; }
    public String getFlatType() { return flatType; }
    public double getAmountPaid() { return amountPaid; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getReferenceNumber() { return referenceNumber; }

    // Setters
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    private String generateReferenceNumber() {
        return "REF-" + System.currentTimeMillis();
    }

    public String generateReceiptText() {
        return String.format(
                "=== HDB BTO Receipt ===\n" +
                        "Receipt ID: %s\n" +
                        "Project: %s\n" +
                        "Flat Type: %s\n" +
                        "Amount Paid: $%.2f\n" +
                        "Payment Date: %s\n" +
                        "Payment Method: %s\n" +
                        "Reference No: %s\n" +
                        "Purchaser: %s (%s)",
                receiptId,
                project.getName(),
                flatType,
                amountPaid,
                paymentDate.toString(),
                paymentMethod != null ? paymentMethod : "Not specified",
                referenceNumber,
                user.getName(),
                user.getNric()
        );
    }

    @Override
    public String toString() {
        return generateReceiptText();
    }
}