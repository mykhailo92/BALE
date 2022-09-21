module com.example {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    requires javafx.web;
    requires jdk.jsobject;

    opens com.example to javafx.fxml;
    exports com.example;
}