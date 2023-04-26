package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingFitDoneMessage extends AbstractMessage {
    public EyetrackingFitDoneMessage(String message) {
        super(message, 2);
    }
}
