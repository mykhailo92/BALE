package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class InformationMessage extends AbstractMessage {
    public InformationMessage(String message) {
        super(message, 2);
    }
}
