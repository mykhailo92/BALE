package de.bale.ui.dialogs;

import de.bale.language.Localizations;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.util.Pair;

/**
 * Creates a EditOrDeleteEntry Dialog which return either a Pair (String,String) with the new Values or
 * a Pair with key == "" and value =="" if the Entry is to be deleted
 */
public class EditOrDeleteEntryDialog extends BaseEntryDialog {


    public EditOrDeleteEntryDialog(String windowName, String name, String path) {
        super(windowName, name, path);
        ButtonType delete = new ButtonType(Localizations.getLocalizedString("deleteButton"), ButtonBar.ButtonData.OTHER);
        getDialogPane().getButtonTypes().add(delete);

        setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.FINISH && !titleField.getText().equals("") && !pathField.getText().equals("")) {
                return new Pair<>(titleField.getText(), pathField.getText());
            } else if (dialogButton == delete) {
                return new Pair<>("", "");
            } else {
                return null;
            }
        });
    }


}
