package be.pxl.services.services;

import be.pxl.services.domain.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationService.class.getName());

    public void sendMessage(Notification notification) {
        log.info("Receiving notification...");
        log.info("Sending...{}", notification.getMessage());
        log.info("To...{}", notification.getSender());
//        System.out.println("Receiving notification...");
//        System.out.println("Sending " + notification.getMessage());
//        System.out.println("To " + notification.getSender());
    }
}
