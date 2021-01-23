package main.app.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import main.logic.cplex.TransportationSolver;
import main.logic.utils.TableGenerator;

import java.net.URL;
import java.util.ResourceBundle;

public class InitViewController implements Initializable {

    private static final int MAX_VALUE = 1000000;


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
    private TableGenerator gen;

    private void generateTable() {
        gen = new TableGenerator(supplyNumber, demandNumber);
        generatedTable = new TableView<>(gen.generateData());
        generatedTable.getColumns().setAll(gen.createColumns());
        generatedTable.setEditable(true);
        customizeTable(generatedTable);
        inputTablePane.getChildren().add(generatedTable);
    }

    private void customizeTable(TableView<String[]> table) {
        generatedTable.setPrefWidth(400);
        generatedTable.setMaxWidth(400);
        generatedTable.setPrefHeight(400);
        generatedTable.setMaxHeight(400);
    }

    public void transitionToSolutionPane() {
        solve();
        inputPane.setVisible(false);
        solutionPane.setVisible(true);
    }


    @FXML private Pane solutionPane;
    @FXML private Pane solutionTablePane;
    TableView<String[]> solutionTable;
    Integer[] supplies;
    Integer[] demands;
    private TransportationSolver solver;

    private void solve() {
        demands = new Integer[demandNumber];
        supplies = new Integer[supplyNumber];
        Integer[][] costs = new Integer[supplyNumber][demandNumber];

        for (int r=0; r<supplyNumber+1; r++) {
            for (int c=0; c<demandNumber+1; c++) {
                int value;
                try {
                    value = Integer.parseInt(generatedTable.getItems().get(r)[c]);
                } catch (NumberFormatException ex) {
                    value = MAX_VALUE;
                }
                if (!(r == 0 && c == 0)) {
                    if (r == 0) {
                        demands[c-1] = value;
                    } else if (c == 0) {
                        supplies[r-1] = value;
                    } else {
                        costs[r-1][c-1] = value;
                    }
                }
            }
        }

        solver = new TransportationSolver();
        solver.solve(supplies, demands, costs);
        visualizeSolution();
    }

    private void visualizeSolution() {
        createSolutionTable();
    }

    private void createSolutionTable() {
        solutionTable = new TableView<>(gen.generateData(supplies, demands, solver.getSolution()));
        solutionTable.getColumns().setAll(gen.createColumns());
        solutionTable.setEditable(false);
        customizeTable(solutionTable);
        solutionTablePane.getChildren().add(solutionTable);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPane.setVisible(true);
        invalidLabel.setVisible(false);
        inputPane.setVisible(false);
        solutionPane.setVisible(false);
    }

}
