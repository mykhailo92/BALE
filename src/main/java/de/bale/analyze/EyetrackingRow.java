package de.bale.analyze;

/**
 * A Row of Eyetracking Information which will be saved in an Excel sheet
 */
public class EyetrackingRow {

    private int posX, posY;
    private String aoi;
    private long duration;

    public EyetrackingRow(int posX, int posY, String aoi, long duration) {
        this.posX = posX;
        this.posY = posY;
        this.aoi = aoi;
        this.duration = duration;
    }

    public int getPosY() {
        return posY;
    }

    public String getAoi() {
        return aoi;
    }

    public int getPosX() {
        return posX;
    }

    public long getDuration() {
        return duration;
    }
}
