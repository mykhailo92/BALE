package de.bale.messages.eyetracking;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingFitDoneMessage extends AbstractMessage {
    public EyetrackingFitDoneMessage(String message) {
        super(message, 2);
    }
}
