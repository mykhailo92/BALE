package de.bale.messages.eyetracking;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingHelpMessage extends AbstractMessage {
    String aoiKey;
    public EyetrackingHelpMessage(String key) {
        super("Need help for: " + key, 2);
        aoiKey = key;
    }

    public String getAoiKey() {
        return aoiKey;
    }
}
