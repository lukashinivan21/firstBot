package tgbots.firstbot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tgbots.firstbot.model.Notification;
import tgbots.firstbot.repository.NotificationRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final NotificationRepository notificationRepository;

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    private final String start = "/start";
    private final String greetingMessage = "Hello, user! I'm glad to greet you!";
    private final Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s*)([\\W+]+)");


    public TelegramBotUpdatesListener(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updatesList) {
        updatesList.forEach(update -> {
            logger.info("Processing update: {}", update);
            String inputMessage = update.message().text();
            Long chatId = update.message().chat().id();
            if (inputMessage.equals(start)) {
                telegramBot.execute(new SendMessage(chatId, greetingMessage));
            }
            Matcher matcher = pattern.matcher(inputMessage);
            if (matcher.matches()) {
                String date = matcher.group(1);
                String message = matcher.group(3);
                LocalDateTime result = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                Notification notification = new Notification();
                notification.setChat_id(chatId);
                notification.setMessage(message);
                notification.setNotification_send_time(result);
                notificationRepository.save(notification);
                if (notification.getId() != null) {
                    telegramBot.execute(new SendMessage(chatId, "Вашему сообщению присвоен номер " + notification.getId()));
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void findNotificationsAndSendMessages() {
        LocalDateTime rightNow = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<Notification> notificationList = notificationRepository.currentNotifications(rightNow);
        notificationList.forEach(notification -> {
            Long chatId = notification.getChat_id();
            String outPutMessage = notification.getMessage();
            telegramBot.execute(new SendMessage(chatId, outPutMessage));
        });
    }
}
