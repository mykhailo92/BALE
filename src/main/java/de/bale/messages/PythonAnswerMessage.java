package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class PythonAnswerMessage extends AbstractMessage {

    public PythonAnswerMessage(String message) {
        super("Python answered: " + message, 0);
    }
}
