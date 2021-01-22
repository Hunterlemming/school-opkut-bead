package main.logic.utils;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TableGenerator {

    private final int N_ROWS;
    private final int N_COLS;

    public TableGenerator(int sellerNumber, int buyerNumber) {
        this.N_ROWS = sellerNumber;
        this.N_COLS = buyerNumber + 1;  // first column in each row is the index
    }

    public ObservableList<double[]> generateData() {

        ArrayList<double[]> myList = new ArrayList<>();

        for (int r = 0; r < N_ROWS; r++) {
            double[] myArray = new double[N_COLS];
            for (int c = 0; c < N_COLS; c++) {
                myArray[c] = 0;
            }
            myList.add(myArray);
        }

        return FXCollections.observableArrayList(myList);

//        return FXCollections.observableArrayList(
//                IntStream.range(0, N_ROWS)
//                        .mapToObj(r ->
//                                IntStream.range(0, N_COLS)
//                                        .mapToDouble(c -> {
//                                            if (c == 0) {
//                                                return r;
//                                            }
//                                            return 0;
//                                        })
//                                        .toArray()
//                        ).collect(Collectors.toList())
//        );
    }

    public List<TableColumn<double[], Double>> createColumns() {
        return IntStream.range(0, N_COLS)
                .mapToObj(this::createColumn)
                .collect(Collectors.toList());
    }

    private TableColumn<double[], Double> createColumn(int c) {
        TableColumn<double[], Double> col = new TableColumn<>("C" + (c + 1));
        col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()[c]));

        return col;
    }

}
