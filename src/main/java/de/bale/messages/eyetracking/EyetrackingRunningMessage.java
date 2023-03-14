package de.bale.messages.eyetracking;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingRunningMessage extends AbstractMessage {
    public EyetrackingRunningMessage(String message) {
        super(message, 1);
    }
}
