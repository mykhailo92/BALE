package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class ErrorMessage extends AbstractMessage {
    public ErrorMessage(String message) {
        super("[ERROR] " + message, 1);
    }
}
