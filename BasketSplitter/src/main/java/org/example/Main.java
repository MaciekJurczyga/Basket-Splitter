package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        BasketSplitter basketSplitter = new BasketSplitter("src/main/resources/config.json");
        List<String> items = new ArrayList<>();
        items.add("Cocoa Butter");
        items.add("Tart - Raisin And Pecan");
        items.add("Table Cloth 54x72 White");
        items.add("Flower - Daisies");
        Map<String,List<String>> ans = basketSplitter.split(items);
        for (Map.Entry<String, List<String>> entry : ans.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            System.out.println("Klucz: " + key);

            for (String value : values) {
                System.out.println("Wartość: " + value);
            }
            System.out.println();
        }
    }
}