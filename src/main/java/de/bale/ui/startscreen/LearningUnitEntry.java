package de.bale.ui.startscreen;

import javafx.beans.property.SimpleStringProperty;

public class LearningUnitEntry {
    SimpleStringProperty learningUnitTitle;
    SimpleStringProperty learningUnitPath;

    public LearningUnitEntry(String title, String path) {
        this.learningUnitTitle = new SimpleStringProperty(title);
        this.learningUnitPath = new SimpleStringProperty(path);
    }

    public String getLearningUnitTitle() {
        return learningUnitTitle.get();
    }

    public SimpleStringProperty learningUnitTitleProperty() {
        return learningUnitTitle;
    }

    public void setLearningUnitTitle(String learningUnitTitle) {
        this.learningUnitTitle.set(learningUnitTitle);
    }

    public String getLearningUnitPath() {
        return learningUnitPath.get();
    }

    public SimpleStringProperty learningUnitPathProperty() {
        return learningUnitPath;
    }

    public void setLearningUnitPath(String learningUnitPath) {
        this.learningUnitPath.set(learningUnitPath);
    }

}
