package de.bale.logger;

import com.google.common.eventbus.Subscribe;
import de.bale.messages.base.AbstractMessage;

public class ConsoleListener {

    int currentLogLevel;

    public ConsoleListener(int logLevel) {
        currentLogLevel = logLevel;
    }

    @Subscribe
    public void gotMessage(AbstractMessage message) {
        if (currentLogLevel <= message.getLogLevel()) {
            System.out.println("[" + message.getMessageDateTimeFormatted() + "] " + message.getMessage());
        }
    }
}
