package de.bale.messages;

import de.bale.messages.base.AbstractMessage;

public class ExperimentEndMessage extends AbstractMessage {
    private int experimentID;
    private String experimentTitle;
    private String childName;

    public ExperimentEndMessage(String experimentTitle, int experimentID, String childName) {
        super("The experiment titled: " + experimentTitle + " with ID: " + experimentID + " has ended.", 2);
        this.experimentID = experimentID;
        this.experimentTitle = experimentTitle;
        this.childName = childName;
    }

    public int getExperimentID() {
        return experimentID;
    }

    public void setExperimentID(int experimentID) {
        this.experimentID = experimentID;
    }

    public String getExperimentTitle() {
        return this.experimentTitle;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildName() {
        return childName;
    }
}
