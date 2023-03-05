package de.bale.repository.feedback;

public class Feedback {

    private int experimentID;
    private String description;
    private String date;
    private int attempts;
    private String comments;

    public Feedback(int experimentID, String description, String date, int attempts, String comments) {
        this.experimentID = experimentID;
        this.description = description;
        this.date = date;
        this.attempts = attempts;
        this.comments = comments;
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

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
