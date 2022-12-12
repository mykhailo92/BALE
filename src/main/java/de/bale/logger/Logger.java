package de.bale.logger;

import com.google.common.eventbus.EventBus;

public class Logger extends EventBus {

    private static Logger instance;

    private Logger() {
        register(new ConsoleListener());
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }
}
