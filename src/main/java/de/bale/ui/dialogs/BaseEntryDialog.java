package de.bale.ui.dialogs;

import de.bale.Utils;
import de.bale.language.Localizations;
import de.bale.ui.SceneHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Baseclass for the Entry Dialogs, the specific Classes specify different ResultConverter and have different Buttons
 */
abstract class BaseEntryDialog extends Dialog {
    Stage stage;
    FileChooser fileChooser;

    protected TextField titleField;
    protected TextField pathField;
    protected Button fileChooserButton;
    protected GridPane grid;
    protected Button finishButton;

    BaseEntryDialog(String windowName, String name, String path) {
        initShared();
        setupVisuals(windowName);
        titleField.setText(name);
        pathField.setText(path);
    }

    BaseEntryDialog(String windowName) {
        initShared();
        setupVisuals(windowName);
    }

    private void initShared() {
        titleField = new TextField();
        pathField = new TextField();
        fileChooserButton = new Button("...");
        grid = new GridPane();
        fileChooser = new FileChooser();
        stage = (Stage) getDialogPane().getScene().getWindow();
        getDialogPane().getButtonTypes().addAll(ButtonType.FINISH, ButtonType.CANCEL);
        finishButton = (Button) getDialogPane().lookupButton(ButtonType.FINISH);

        //Making FileChooser
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("HTML (*.html)", "*.html");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooserButton.setOnMousePressed((event) -> {
            File learningUnitFile = fileChooser.showOpenDialog(stage);
            if (learningUnitFile != null) {
                pathField.setText(learningUnitFile.getAbsolutePath());
            }
        });
        finishButton.addEventFilter(ActionEvent.ACTION, this::checkEmptyFields);

        grid.add(new Label(Localizations.getLocalizedString("nameColumn")), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label(Localizations.getLocalizedString("pathColumn")), 0, 1);
        grid.add(pathField, 1, 1);
        grid.add(fileChooserButton, 2, 1);

        getDialogPane().setContent(grid);
    }

    private void setupVisuals(String windowName) {
        setTitle(windowName);
        grid.setHgap(5);
        grid.setVgap(5);
        pathField.setEditable(false);

        getDialogPane().getStyleClass().add("myDialog");
        getDialogPane().getStylesheets().add(Utils.getStylesheetPath(SceneHandler.getInstance().getThemeName()));
        getDialogPane().getStylesheets().add(Utils.getStylesheetPath("fxmlStyle"));
    }

    protected void checkEmptyFields(ActionEvent actionEvent) {
        if (titleField.getText().equals("") || pathField.getText().equals("")) {
            Alert emptyFields = new Alert(Alert.AlertType.ERROR);
            emptyFields.show();
            emptyFields.setHeaderText(Localizations.getLocalizedString("newEntryErrorEmptyHeader"));
            emptyFields.setContentText(Localizations.getLocalizedString("newEntryErrorEmptyContent"));
            actionEvent.consume(); //empty field
        }
    }
}
