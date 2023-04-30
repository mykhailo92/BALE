package de.bale.messages.eyetracking;

import de.bale.messages.base.AbstractMessage;

public class EyetrackingAoIMessage extends AbstractMessage {
    private String aoi;
    int x, y, experimentiD;

    public EyetrackingAoIMessage(String aoi, int x, int y, int experimentID) {
        super("New AoI Target: " + aoi + " " + x + " " + y, 2);
        this.aoi = aoi;
        this.x = x;
        this.y = y;
        this.experimentiD = experimentID;
    }

    public String getAoi() {
        return aoi;
    }

    public void setAoi(String aoi) {
        this.aoi = aoi;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getExperimentID() {
        return experimentiD;
    }

    public void setExperimentiD(int experimentiD) {
        this.experimentiD = experimentiD;
    }
}
