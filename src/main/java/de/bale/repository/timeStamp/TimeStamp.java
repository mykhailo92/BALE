package de.bale.repository.timeStamp;

public class TimeStamp {

    private int experimentID;
    private String description;
    private String date;

    public TimeStamp(int experimentID, String description, String date) {
        this.experimentID = experimentID;
        this.description = description;
        this.date = date;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(int experimentID) {
        this.experimentID = experimentID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
