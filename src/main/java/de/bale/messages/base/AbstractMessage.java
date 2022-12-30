package de.bale.messages.base;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractMessage {
    private LocalDateTime messageDateTime;
    private String message;
    private int logLevel;

    protected AbstractMessage(String message,int logLevel) {
        this.message=message;
        this.messageDateTime = LocalDateTime.now();
        this.logLevel = logLevel;
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
    public int getLogLevel() {
        return logLevel;
    }
}
