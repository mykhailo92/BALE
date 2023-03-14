package de.bale.messages.eyetracking;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingCallibrationMessage extends AbstractMessage {
    private float clickX;
    private float clickY;

    public EyetrackingCallibrationMessage(float clickX, float clickY) {
        super("Start Callibration", 0);
        this.clickX = clickX;
        this.clickY = clickY;
    }

    public float getClickX() {
        return clickX;
    }

    public void setClickX(float clickX) {
        this.clickX = clickX;
    }

    public float getClickY() {
        return clickY;
    }

    public void setClickY(float clickY) {
        this.clickY = clickY;
    }
}
