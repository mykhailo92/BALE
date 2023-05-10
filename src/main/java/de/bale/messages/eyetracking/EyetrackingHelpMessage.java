package de.bale.messages.eyetracking;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingHelpMessage extends AbstractMessage {
    String aoiKey;
    public EyetrackingHelpMessage(String key) {
        super("", -1);
        aoiKey = key;
    }

    public String getAoiKey() {
        return aoiKey;
    }
}
