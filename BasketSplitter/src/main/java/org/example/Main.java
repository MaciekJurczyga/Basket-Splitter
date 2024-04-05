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
        items.add("Cookies Oatmeal Raisin");
        items.add("Cheese - Mix");
        items.add("Fish - Soup Base, Bouillon");
        items.add("Fond - Chocolate");
        items.add("Beans - Green");
        Map<String,List<String>> ans = basketSplitter.split(items);
        for (Map.Entry<String, List<String>> entry : ans.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();

            System.out.println("Key: " + key);

            for (String value : values) {
                System.out.println("  Value: " + value);
            }
            System.out.println();
        }
    }
}