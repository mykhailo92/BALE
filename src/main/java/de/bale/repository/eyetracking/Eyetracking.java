package de.bale.repository.eyetracking;


public class Eyetracking {
    private int experimentID;
    private String description;
    private String answer;
    private int solutionAttempts;

    public Eyetracking(int id, String description, String answer, int attempts) {
        this.experimentID = id;
        this.description = description;
        this.answer = answer;
        this.solutionAttempts = attempts;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getSolutionAttempts() {
        return solutionAttempts;
    }

    public void setSolutionAttempts(int solutionAttempts) {
        this.solutionAttempts = solutionAttempts;
    }
}
