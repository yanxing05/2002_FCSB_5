package bto.system.services;

import bto.system.models.Notification;
import bto.system.models.users.User;
import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final List<Notification> notifications;

    public NotificationService() {
        this.notifications = new ArrayList<>();
    }

    public void sendNotification(User recipient, String title, String message) {
        Notification notification = new Notification(
                recipient,
                title,
                message,
                Notification.NotificationType.SYSTEM_ALERT
        );
        notifications.add(notification);
        recipient.addNotification(notification);
    }

    public List<Notification> getUnreadNotifications(User user) {
        return notifications.stream()
                .filter(n -> n.getRecipient().equals(user) && !n.isRead())
                .toList();
    }

    public void markAsRead(String notificationId) {
        notifications.stream()
                .filter(n -> n.getNotificationId().equals(notificationId))
                .findFirst()
                .ifPresent(Notification::markAsRead);
    }
}