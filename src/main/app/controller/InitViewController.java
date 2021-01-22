package main.app.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import main.logic.utils.TableContent;
import main.logic.utils.TableGenerator;
import main.logic.utils.TableStringGenerator;

import java.net.URL;
import java.util.ArrayList;
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
        inputTablePane.setVisible(true);
    }


    @FXML private Pane inputTablePane;
    @FXML private TableView inputTable;
    TableView<ArrayList<String>> myTable = new TableView<>();
    TableView<double[]> myIntTable;
    TableView<String[]> myStringTable;

    private void generateTable() {

//        TableGenerator gen = new TableGenerator(supplyNumber, demandNumber);

//        ObservableList<double[]> data = gen.generateData();
//        myIntTable = new TableView<>(data);
//        myIntTable.getColumns().setAll(gen.createColumns());
//        inputTablePane.getChildren().add(myIntTable);


        TableStringGenerator genS = new TableStringGenerator(supplyNumber, demandNumber);
        myStringTable = new TableView<>(genS.generateData());
        myStringTable.getColumns().setAll(genS.createColumns());
        inputTablePane.getChildren().add(myStringTable);


    }

//    private void letee() {
//        for (int i = -1; i < demandNumber; i++) {
//            TableColumn<ArrayList<String>, String> column;
//            if (i == -1) {
//                column = new TableColumn<>("Seller\\Buyer");
//            } else {
//                column = new TableColumn<>("" + i);
//            }
//            column.setCellValueFactory(c -> new SimpleStringProperty);
//            myTable.getColumns().add(column);
//        }
//        inputTablePane.getChildren().add(myTable);
//        later();
//        System.out.println("stop");
//    }
//
//    private void later() {  //TODO
//        TableContent initialTableContent = new TableContent(supplyNumber, demandNumber);
//        ObservableList<ArrayList<String>> initialValues = FXCollections.observableArrayList();
//        for (int i = 0; i < supplyNumber; i++) {
//            initialValues.add(initialTableContent.getCostRow(i));
//        }
////        inputTable.setItems(initialValues);
//        myTable.setItems(initialValues);
//    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPane.setVisible(true);
        invalidLabel.setVisible(false);
        inputTablePane.setVisible(false);
    }

}
