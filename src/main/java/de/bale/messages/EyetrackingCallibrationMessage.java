package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingCallibrationMessage extends AbstractMessage {
    public EyetrackingCallibrationMessage() {
        super("Start Callibration", 0);
    }
}
