package de.bale.messages.eyetracking;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingStoppingMessage extends AbstractMessage {
    public EyetrackingStoppingMessage(String message) {
        super(message, 1);
    }
}
