package de.bale.ui.dialogs;

import de.bale.Utils;
import de.bale.language.Localizations;
import de.bale.ui.SceneHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.File;

/**
 * Creates a EditOrDeleteEntry Dialog which return either a Pair (String,String) with the new Values or
 * a Pair with key == "" and value =="" if the Entry is to be deleted
 */
public class EditOrDeleteEntryDialog extends Dialog {
    private final Stage stage;
    private final FileChooser fileChooser;

    public EditOrDeleteEntryDialog(String windowName, String name, String path) {
        //Init
        TextField titleField = new TextField();
        TextField pathField = new TextField();
        Button fileChooserButton = new Button("...");
        GridPane grid = new GridPane();
        fileChooser = new FileChooser();
        stage = (Stage) getDialogPane().getScene().getWindow();
        ButtonType delete = new ButtonType(Localizations.getLocalizedString("deleteButton"), ButtonBar.ButtonData.OTHER);
        getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CANCEL, delete);
        Button finishButton = (Button) getDialogPane().lookupButton(ButtonType.FINISH);

        getDialogPane().getStyleClass().add("myDialog");
        getDialogPane().getStylesheets().add(Utils.getStylesheetPath(SceneHandler.getInstance().getThemeName()));
        getDialogPane().getStylesheets().add(Utils.getStylesheetPath("fxmlStyle"));

        titleField.setText(name);
        pathField.setText(path);
        //Create Visuals
        setTitle(windowName);
        grid.setHgap(5);
        grid.setVgap(5);
        pathField.setEditable(false);

        //Preventing to add empty Fields
        finishButton.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            if (titleField.getText().equals("") || pathField.getText().equals("")) {
                Alert emptyFields = new Alert(Alert.AlertType.ERROR);
                emptyFields.show();
                emptyFields.setHeaderText(Localizations.getLocalizedString("newEntryErrorEmptyHeader"));
                emptyFields.setContentText(Localizations.getLocalizedString("newEntryErrorEmptyContent"));
                actionEvent.consume(); //empty field
            }
        });
        //Making FileChooser
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("HTML (*.html)", "*.html");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooserButton.setOnMousePressed((event) -> {
            File learningUnitFile = fileChooser.showOpenDialog(stage);
            if (learningUnitFile != null) {
                pathField.setText(learningUnitFile.getAbsolutePath());
            }
        });

        //Adding Elements
        grid.add(new Label(Localizations.getLocalizedString("nameColumn")), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label(Localizations.getLocalizedString("pathColumn")), 0, 1);
        grid.add(pathField, 1, 1);
        grid.add(fileChooserButton, 2, 1);

        getDialogPane().setContent(grid);

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
