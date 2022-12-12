package de.bale.messages.base;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractMessage {
    private LocalDateTime messageDateTime;
    private String message;

    protected AbstractMessage(String message) {
        this.message=message;
        this.messageDateTime = LocalDateTime.now();
    }

    public LocalDateTime getMessageDateTime() {
        return messageDateTime;
    }

    public String getMessageDateTimeFormatted() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[yyyy/MM/dd HH:mm:ss]");
        return messageDateTime.format(dtf);
    }

    public void setMessageDateTime(LocalDateTime messageDateTime) {
        this.messageDateTime = messageDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
