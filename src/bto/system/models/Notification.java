package bto.system.models;

import bto.system.models.users.User;
import java.time.LocalDateTime;

public class Notification {
    public enum NotificationType {
        APPLICATION_STATUS,
        PROJECT_UPDATE,
        PAYMENT_REMINDER,
        SYSTEM_ALERT
    }

    private String notificationId;
    private User recipient;
    private String title;
    private String content;
    private NotificationType type;
    private LocalDateTime createdAt;
    private boolean isRead;

    public Notification(User recipient, String title, String content, NotificationType type) {
        this.notificationId = "NOT-" + System.currentTimeMillis();
        this.recipient = recipient;
        this.title = title;
        this.content = content;
        this.type = type;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
    }

    // Getters
    public String getNotificationId() { return notificationId; }
    public User getRecipient() { return recipient; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public NotificationType getType() { return type; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isRead() { return isRead; }

    // Setters
    public void markAsRead() {
        this.isRead = true;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public String getSummary() {
        return String.format("[%s] %s - %s",
                type,
                title,
                createdAt.toLocalDate().toString());
    }

    @Override
    public String toString() {
        return String.format("Notification #%s\nType: %s\nTitle: %s\nContent: %s\nTime: %s\nStatus: %s",
                notificationId,
                type,
                title,
                content,
                createdAt.toString(),
                isRead ? "Read" : "Unread"
        );
    }
}
