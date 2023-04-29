package de.bale.repository.eyetracking;


public class Eyetracking {
    private int experimentID;
    private int viewID;
    private int x;
    private int y;
    private String timeStamp;
    private String element;
    private long durationInMS;

    public Eyetracking(int id, int viewID, int x, int y, String timeStamp, String element, long durationInMs) {
        this.experimentID = id;
        this.viewID = viewID;
        this.x = x;
        this.y = y;
        this.timeStamp = timeStamp;
        this.element = element;
        this.durationInMS = durationInMs;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(int experimentID) {
        this.experimentID = experimentID;
    }

    public int getViewID() {
        return viewID;
    }

    public void setViewID(int viewID) {
        this.viewID = viewID;
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

    public long getViewDurationInMS() {
        return durationInMS;
    }

    public void setViewDurationInMS(long durationInMS) {
        this.durationInMS = durationInMS;
    }
}
