package de.bale.messages.eyetracking;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingCallibrationDoneMessage extends AbstractMessage {
    public EyetrackingCallibrationDoneMessage() {
        super("Eyetracking Callibration is Done", 2);
    }
}
