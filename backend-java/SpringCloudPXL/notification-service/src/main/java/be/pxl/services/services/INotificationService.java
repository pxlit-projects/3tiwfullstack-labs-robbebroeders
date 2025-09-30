package be.pxl.services.services;

import be.pxl.services.domain.Notification;

import java.util.List;

public interface INotificationService {
    List<Notification> getAllNotifications();
}
