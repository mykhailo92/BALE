package de.bale.logger;

import com.google.common.eventbus.EventBus;
import de.bale.messages.LogLevelMessage;
import de.bale.messages.SessionStartMessage;
import de.bale.storage.PropertiesUtils;

import java.util.Properties;

public class Logger extends EventBus {

    private static Logger instance;
    private ConsoleListener listener;

    private Logger() {
        Properties settings = PropertiesUtils.getSettingsProperties();
        int loglevel;
        try {
            loglevel = Integer.parseInt(settings.getProperty("loglevel"));
        } catch (NumberFormatException e) {
            loglevel = 0;
        }
        listener = new ConsoleListener(loglevel);
        register(listener);
        post(new SessionStartMessage());
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void changeLogLevel(int loglevel) {
        post(new LogLevelMessage(loglevel));
        listener.currentLogLevel = loglevel;
    }
}