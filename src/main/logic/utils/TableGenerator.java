package main.logic.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TableGenerator {

    private final int N_ROWS;
    private final int N_COLS;

    public TableGenerator(int sellerNumber, int buyerNumber) {
        this.N_ROWS = sellerNumber + 1; // first row of each column is the current demand
        this.N_COLS = buyerNumber + 1;  // first column in each row is the current supply
    }

    public ObservableList<String[]> generateData() {

        ArrayList<String[]> myList = new ArrayList<>();

        for (int r = 0; r < N_ROWS; r++) {
            String[] myArray = new String[N_COLS];
            for (int c = 0; c < N_COLS; c++) {
                if (c == 0 && r == 0) {
                    myArray[c] = "Costs";
                } else {
                    myArray[c] = "0";       // Supply = Seller
                }
            }
            myList.add(myArray);
        }

        return FXCollections.observableArrayList(myList);
    }

    public ObservableList<String[]> generateData(Integer[] supply, Integer[] demand, double[][] solution) {

        ArrayList<String[]> myList = new ArrayList<>();

        for (int r = 0; r < N_ROWS; r++) {
            String[] myArray = new String[N_COLS];
            for (int c = 0; c < N_COLS; c++) {
                if (c == 0 && r == 0) {
                    myArray[c] = "Solution";
                } else {                        // Supply = Seller
                    if (r == 0) {
                        myArray[c] = "" + demand[c-1];
                    } else if (c == 0) {
                        myArray[c] = "" + supply[r-1];
                    } else {
                        myArray[c] = "" + solution[r-1][c-1];
                    }
                }
            }
            myList.add(myArray);
        }

        return FXCollections.observableArrayList(myList);
    }

    public List<TableColumn<String[], String>> createColumns() {
        return IntStream.range(0, N_COLS)
                .mapToObj(this::createColumn)
                .collect(Collectors.toList());
    }

    private TableColumn<String[], String> createColumn(int c) {
        TableColumn<String[], String> col;
        if (c == 0) {
            col = new TableColumn<>("Supplies");
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[c]));
            col.setPrefWidth(80);
        } else {
            col = new TableColumn<>("D-" + c);  // Demand = Buyer
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue()[c]));
            col.setPrefWidth(40);
        }
        col.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<String[], String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<String[], String> stringCellEditEvent) {
                        ((String[]) stringCellEditEvent.getTableView().getItems().get(
                                stringCellEditEvent.getTablePosition().getRow())
                        )[c] = stringCellEditEvent.getNewValue();
                    }
                }
        );
        col.setStyle("-fx-alignment: CENTER;");
        col.setMinWidth(40);
        col.setMaxWidth(80);

        return col;
    }

}
