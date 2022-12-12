package de.bale.logger;

import com.google.common.eventbus.Subscribe;
import de.bale.messages.base.AbstractMessage;

public class ConsoleListener {

    @Subscribe
    public void gotMessage(AbstractMessage message) {
        System.out.println("["+message.getMessageDateTimeFormatted() + "] " + message.getMessage());
    }
}
