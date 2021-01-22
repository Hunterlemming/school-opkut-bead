package main.logic.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TableStringGenerator {

    private final int N_ROWS;
    private final int N_COLS;

    public TableStringGenerator(int sellerNumber, int buyerNumber) {
        this.N_ROWS = sellerNumber;
        this.N_COLS = buyerNumber + 1;  // first column in each row is the index
    }

    public ObservableList<String[]> generateData() {

        ArrayList<String[]> myList = new ArrayList<>();

        for (int r = 0; r < N_ROWS; r++) {
            String[] myArray = new String[N_COLS];
            for (int c = 0; c < N_COLS; c++) {
                if (c == 0) {
                    myArray[c] = "Seller " + r;
                } else {
                    myArray[c] = "0";
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
        TableColumn<String[], String> col = new TableColumn<>("C" + (c + 1));
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[c]));

        return col;
    }

}
