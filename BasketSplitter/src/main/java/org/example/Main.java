package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        BasketSplitter basketSplitter = new BasketSplitter("src/main/resources/config.json");
        List<String> deliveryOptions= basketSplitter.getDeliveryMethods("Cocoa Butter");
        for(String option:deliveryOptions){
            System.out.println(option);

        }
    }
}