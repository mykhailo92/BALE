package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingStartMessage extends AbstractMessage {
    public EyetrackingStartMessage() {
        super("Eyetracking has started", 1);
    }
}
