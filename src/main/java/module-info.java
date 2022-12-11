module de.bale {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    requires javafx.web;
    requires jdk.jsobject;
    requires javafx.media;

    opens de.bale.ui to javafx.fxml;
    exports de.bale.ui;
    exports de.bale;
    opens de.bale to javafx.fxml;
    exports de.bale.ui.interfaces;
    opens de.bale.ui.interfaces to javafx.fxml;
    opens de.bale.ui.startscreen to javafx.fxml, javafx.base;
    opens de.bale.ui.startscreen.interfaces to javafx.fxml;
    opens de.bale.settings;
    exports de.bale.ui.learningUnit;
    opens de.bale.ui.learningUnit to javafx.fxml;
    exports de.bale.ui.learningUnit.interfaces;
    opens de.bale.ui.learningUnit.interfaces to javafx.fxml;
    opens de.bale.ui.dialogs to javafx.base, javafx.fxml;
}