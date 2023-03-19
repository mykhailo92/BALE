package de.bale.messages.eyetracking;

import de.bale.messages.base.AbstractMessage;

public class WriteToPythonMessage extends AbstractMessage {
    public WriteToPythonMessage(String message) {
        super(message, -1);
    }
}
