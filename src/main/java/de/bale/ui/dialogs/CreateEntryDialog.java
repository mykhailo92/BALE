package de.bale.ui.dialogs;

import javafx.scene.control.ButtonType;
import javafx.util.Pair;

/**
 * Creates a Entry Dialog which returns a Pair (String,String) of a new LearningUnitEntry
 * The Key is the Name of the Unit and the Value is the Path
 */
public class CreateEntryDialog extends BaseEntryDialog {

    public CreateEntryDialog(String windowName) {
        super(windowName);
        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.FINISH && !titleField.getText().equals("") && !pathField.getText().equals("")) {
                return new Pair<>(titleField.getText(), pathField.getText());
            }
            return null;
        });
    }
}
