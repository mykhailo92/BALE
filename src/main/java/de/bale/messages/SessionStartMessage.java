package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class SessionStartMessage extends AbstractMessage {
    public SessionStartMessage() {
        super("====New Session started====", 4);
    }
}
