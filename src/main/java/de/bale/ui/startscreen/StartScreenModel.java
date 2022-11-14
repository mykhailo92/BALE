package de.bale.ui.startscreen;

import de.bale.ui.startscreen.interfaces.IStartScreenModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StartScreenModel implements  IStartScreenModel {


    private ObservableList<LearningUnitEntry> entries = FXCollections.observableArrayList();

    @Override
    public void addEntry(String title, String path) {
        entries.add(new LearningUnitEntry(title, path));
    }

    @Override
    public ObservableList<LearningUnitEntry> getEntries() {
        return entries;
    }

    @Override
    public void addEntry(LearningUnitEntry exampleEntry) {
        entries.add(exampleEntry);
    }
}
