package tgbots.firstbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tgbots.firstbot.model.Notification;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT * FROM Notification WHERE (Notification.notification_send_time = ?1)", nativeQuery = true)
    List<Notification> currentNotifications(LocalDateTime current);
}
