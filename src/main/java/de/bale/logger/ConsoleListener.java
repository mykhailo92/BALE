package de.bale.logger;

import com.google.common.eventbus.Subscribe;
import de.bale.messages.base.AbstractMessage;
import de.bale.storage.WriteLog;

public class ConsoleListener {

    protected int currentLogLevel;
    private WriteLog writeLog;

    public ConsoleListener(int logLevel) {
        currentLogLevel = logLevel;
        writeLog = new WriteLog();
    }

    @Subscribe
    public void gotMessage(AbstractMessage message) {
        if (currentLogLevel <= message.getLogLevel()) {
            String logMessage = "[" + message.getMessageDateTimeFormatted() + "] " + message.getMessage();
            System.out.println(logMessage);
            writeLog.addLinetoAllLog(logMessage);
        }
    }
}
