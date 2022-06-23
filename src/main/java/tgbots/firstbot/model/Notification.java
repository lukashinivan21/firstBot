package tgbots.firstbot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long chat_id;
    private String message;
    private LocalDateTime notification_send_time;

    public Notification(Long id, Long chat_id, String message, LocalDateTime notification_send_time) {
        this.id = id;
        this.chat_id = chat_id;
        this.message = message;
        this.notification_send_time = notification_send_time;
    }

    public Notification() {

    }

    public Long getId() {
        return id;
    }

    public Long getChat_id() {
        return chat_id;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getNotification_send_time() {
        return notification_send_time;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNotification_send_time(LocalDateTime notification_send_time) {
        this.notification_send_time = notification_send_time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
