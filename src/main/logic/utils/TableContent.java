package main.logic.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class TableContent {

    private final HashMap<Integer, ArrayList<String>> sellerCosts = new HashMap<>();

    // Sellers - Rows
    // Buyers - Columns
    public TableContent(int sellerNumber, int buyerNumber) {
        for (int sellerId = 0; sellerId < sellerNumber; sellerId++) {
            ArrayList<String> columns = new ArrayList<>();
            columns.add("" + sellerId);
            for (int buyerId = 0; buyerId < buyerNumber; buyerId++) {
                columns.add("0");
            }
            sellerCosts.put(sellerId, columns);
        }
    }

    public String getCost(int sellerId, int buyerId) {
        return sellerCosts.get(sellerId).get(buyerId + 1);  // first column in each row is the index
    }

    public ArrayList<String> getCostRow(int sellerId) {
        return sellerCosts.get(sellerId);
    }

    public void setCost(int sellerId, int buyerId, String value) {
        ArrayList<String> sellerCostsInRow = sellerCosts.get(sellerId);
        sellerCostsInRow.set(buyerId + 1, value);           // first column in each row is the index
        sellerCosts.put(sellerId, sellerCostsInRow);
    }

}
