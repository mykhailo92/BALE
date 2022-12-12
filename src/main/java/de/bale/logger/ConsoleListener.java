package de.bale.logger;

import com.google.common.eventbus.Subscribe;
import de.bale.messages.base.AbstractMessage;
import de.bale.messages.InitMessage;
import de.bale.messages.TaskDoneMessage;

public class ConsoleListener {

    @Subscribe
    public void gotMessage(AbstractMessage message) {
        //Write to a log file...
    }

    @Subscribe
    public void gotInitMessage(InitMessage initMessage) {
//        System.out.println("[" + initMessage.getMessageDateTimeFormated() + "] " + initMessage.getMessage());
    }

    @Subscribe
    public void gotDoneMessage(TaskDoneMessage taskDoneMessage) {
        System.out.println("[" +taskDoneMessage.getMessageDateTimeFormated() + "] " + taskDoneMessage.getMessage());
    }
}
