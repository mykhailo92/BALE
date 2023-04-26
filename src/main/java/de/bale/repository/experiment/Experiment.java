package de.bale.repository.experiment;

public class Experiment {

    private int experimentID;
    private String name;
    private String date;
    private String learnUnit;

    public Experiment(String name, String date, String learnUnit) {
        this.name = name;
        this.date = date;
        this.learnUnit = learnUnit;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(int experimentID) {
        this.experimentID = experimentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLearnUnit() {
        return learnUnit;
    }

    public void setLearnUnit(String learnUnit) {
        this.learnUnit = learnUnit;
    }
}
