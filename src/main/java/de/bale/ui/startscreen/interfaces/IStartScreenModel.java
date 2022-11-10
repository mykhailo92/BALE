package de.bale.ui.startscreen.interfaces;

import de.bale.ui.startscreen.LearningUnitEntry;
import javafx.collections.ObservableList;

public interface IStartScreenModel {
    void addEntry(String title, String path);

    ObservableList<LearningUnitEntry> getEntries();

    void addEntry(LearningUnitEntry exampleEntry);
}
