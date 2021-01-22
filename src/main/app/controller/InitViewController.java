package main.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import main.logic.utils.TableGenerator;

import java.net.URL;
import java.util.ResourceBundle;

public class InitViewController implements Initializable {


    @FXML private Pane initPane;
    @FXML private TextField supplyInputField;
    @FXML private TextField demandInputField;
    @FXML private Label invalidLabel;

    private int supplyNumber;
    private int demandNumber;

    public void saveConfig() {
        try {
            supplyNumber = Integer.parseInt(supplyInputField.getText());
            demandNumber = Integer.parseInt(demandInputField.getText());
            transitionToInputTablePane();
        } catch (NumberFormatException ex) {
            invalidLabel.setVisible(true);
        }
    }

    private void transitionToInputTablePane() {
        generateTable();
        initPane.setVisible(false);
        inputPane.setVisible(true);
    }


    @FXML private Pane inputPane;
    @FXML private Pane inputTablePane;
    TableView<String[]> generatedTable;

    private void generateTable() {
        TableGenerator gen = new TableGenerator(supplyNumber, demandNumber);
        generatedTable = new TableView<>(gen.generateData());
        generatedTable.getColumns().setAll(gen.createColumns());
        generatedTable.setEditable(true);
        generatedTable.setPrefWidth(400);
        generatedTable.setMaxWidth(400);
        generatedTable.setPrefHeight(400);
        generatedTable.setMaxHeight(400);
        inputTablePane.getChildren().add(generatedTable);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPane.setVisible(true);
        invalidLabel.setVisible(false);
        inputPane.setVisible(false);
    }

}
