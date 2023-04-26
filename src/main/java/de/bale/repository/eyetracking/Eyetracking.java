package de.bale.repository.eyetracking;


public class Eyetracking {
    private int experimentID;
    private int blickID;
    private int x;
    private  int y;
    private String timeStamp;
    private String element;

    public Eyetracking(int id, int blickID, int x, int y, String timeStamp, String element) {
        this.experimentID = id;
        this.blickID = blickID;
        this.x = x;
        this.y = y;
        this.timeStamp = timeStamp;
        this.element = element;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(int experimentID) {
        this.experimentID = experimentID;
    }

    public int getBlickID() {
        return blickID;
    }

    public void setBlickID(int blickID) {
        this.blickID = blickID;
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

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }
}
