package de.bale.messages.eyetracking;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingAOIMessage extends AbstractMessage {
    public EyetrackingAOIMessage(String aoi) {
        super("New AoI Target: " + aoi, 2);
    }
}
