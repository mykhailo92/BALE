package de.bale.messages.eyetracking;

import de.bale.messages.base.AbstractMessage;

public class EyeTrackingDataMessage extends AbstractMessage {

    private int x, y;

    public EyeTrackingDataMessage(int x, int y) {
        super("New EyeTracking-position: X=" + x + " Y=" + y, -1);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
