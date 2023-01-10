package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class LogLevelMessage extends AbstractMessage {
    public LogLevelMessage(int loglevel) {
        super("Loglevel was changed to: "+ (loglevel),4);
    }
}
