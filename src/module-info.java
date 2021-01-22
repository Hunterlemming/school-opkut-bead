module BeadTry1 {
    requires javafx.fxml;
    requires javafx.controls;

    opens main.app;
    opens main.app.controller;
    opens main.app.view;
}