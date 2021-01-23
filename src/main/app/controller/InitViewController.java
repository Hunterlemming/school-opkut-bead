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
import java.util.ArrayList;
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
    @FXML private Label parameterErrorLabel;
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
        table.setMinWidth(400);
        table.setPrefWidth(400);
        table.setMaxWidth(400);
        table.setMinHeight(400);
        table.setPrefHeight(400);
        table.setMaxHeight(400);
    }

    public void transitionToSolutionPane() {
        solve();
        if (solver.getIsSolved()) {
            inputPane.setVisible(false);
            solutionPane.setVisible(true);
        }
    }


    @FXML private Pane solutionPane;
    @FXML private Pane solutionTablePane;
    TableView<String[]> solutionTable;
    Integer[] supplies;
    Integer[] demands;
    Integer[][] costs;
    private TransportationSolver solver;
    private ArrayList<String> problemType;

    private void solve() {
        problemType = new ArrayList<>();
        demands = new Integer[demandNumber];
        supplies = new Integer[supplyNumber];
        costs = new Integer[supplyNumber][demandNumber];

        for (int r=0; r<supplyNumber+1; r++) {
            for (int c=0; c<demandNumber+1; c++) {
                int value;
                try {
                    value = Integer.parseInt(generatedTable.getItems().get(r)[c]);
                } catch (NumberFormatException ex) {
                    if (c == 0 && r == 0) {
                        continue;
                    }
                    problemType.add("Prohibited");
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

        checkForOpen();

        solver = new TransportationSolver();
        solver.solve(supplies, demands, costs);
        if (solver.getIsSolved()) {
            visualizeSolution();
        } else {
            parameterErrorLabel.setVisible(true);
        }
    }

    private void checkForOpen() {
        int demandSum = 0;
        int supplySum = 0;

        for (Integer demand : demands) { demandSum += demand; }

        for (Integer supply : supplies) { supplySum += supply; }

        if (demandSum < supplySum) {
            addDemandColumn(supplySum - demandSum);
        } else if (demandSum > supplySum) {
            addSupplierRow(demandSum - supplySum);
        } else {
            problemType.add("Closed");
        }
    }

    private void addDemandColumn(int newDemand) {
        problemType.add("Open");

        Integer[] newDemands = new Integer[demandNumber + 1];
        System.arraycopy(demands, 0, newDemands, 0, demands.length);
        newDemands[demandNumber] = newDemand;

        Integer[][] newCosts = new Integer[supplyNumber][demandNumber + 1];
        for (int r=0; r<supplyNumber; r++) {
            for (int c=0; c<demandNumber + 1; c++) {
                if (c!=demandNumber) {
                    newCosts[r][c] = costs[r][c];
                } else {
                    newCosts[r][c] = 0;
                }
            }
        }

        demands = newDemands;
        costs = newCosts;
    }

    private void addSupplierRow(int newSupplier) {
        problemType.add("Open");

        Integer[] newSuppliers = new Integer[supplyNumber + 1];
        System.arraycopy(supplies, 0, newSuppliers, 0, supplies.length);
        newSuppliers[supplyNumber] = newSupplier;

        Integer[][] newCosts = new Integer[supplyNumber +1][demandNumber];
        for (int r=0; r<supplyNumber + 1; r++) {
            for (int c=0; c<demandNumber; c++) {
                if (r!=supplyNumber) {
                    newCosts[r][c] = costs[r][c];
                } else {
                    newCosts[r][c] = 0;
                }
            }
        }

        supplies = newSuppliers;
        costs = newCosts;
    }



    @FXML private Label solutionStatusLabel;
    @FXML private Label solutionTypeLabel;
    @FXML private Label solutionCostLabel;

    private void visualizeSolution() {
        solutionStatusLabel.setText(solver.getStatus());
        solutionTypeLabel.setText(getSolutionType());
        solutionCostLabel.setText("" + solver.getSolutionCost());
        createSolutionTable();
    }

    private String getSolutionType() {
        StringBuilder sb = new StringBuilder();
        for (String type : problemType) {
            sb.append(type);
            sb.append(" ");
        }
        return sb.toString();
    }

    private void createSolutionTable() {
        solutionTable = new TableView<>(gen.generateData(supplies, demands, solver.getSolution()));
        solutionTable.getColumns().setAll(gen.createColumns());
        solutionTable.setEditable(false);
        customizeTable(solutionTable);
        solutionTablePane.getChildren().add(solutionTable);
    }

    public void reset() {
        supplyInputField.setText("");
        demandInputField.setText("");
        initPane.setVisible(true);
        invalidLabel.setVisible(false);
        inputPane.setVisible(false);
        parameterErrorLabel.setVisible(false);
        solutionPane.setVisible(false);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reset();
    }

}
