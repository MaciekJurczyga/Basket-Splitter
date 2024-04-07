package com.ocado.basket;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import org.apache.logging.log4j.Logger;

/// !!! algorithm is described in readme file !!! ///

public class BasketSplitter {

    private final String absolutePathToConfigFile;
    private boolean invalidInput = false;
    private static final Logger logger = LogManager.getLogger(BasketSplitter.class);

    public BasketSplitter(String absolutePathToConfigFile){
        this.absolutePathToConfigFile = absolutePathToConfigFile;
    }

    public Map<String, List<String>> split(List<String> items){
        Map<String, Integer> deliveryOccurrences = new HashMap<>();
        Map<String, List<String>> answer = new HashMap<>();
        for(String item:items){
            List<String> deliveryOptions = getDeliveryMethods(item);
            if(invalidInput){
                return null;
            }
            for(String option:deliveryOptions){
                if(deliveryOccurrences.containsKey(option)){
                    deliveryOccurrences.put(option, deliveryOccurrences.get(option)+1);
                }
                else{
                    deliveryOccurrences.put(option, 1);
                }
            }
        }

        while(!items.isEmpty()){
            String bestDelivery = getBestDelivery(deliveryOccurrences);
            List<String> products = new ArrayList<>();
            List<String> itemsCopy = new ArrayList<>(items); // copy of items in order to avoid ConcurrentModificationException
            for(String item:itemsCopy){
                List<String> options = getDeliveryMethods(item);
                if(options.isEmpty()){ // in case of an input with non-existing item in config file
                    items.remove(item);
                }
                if(options.contains(bestDelivery)){
                    products.add(item);
                    items.remove(item);
                    for(String option:options){
                        deliveryOccurrences.put(option, deliveryOccurrences.get(option)-1);
                    }
                }
                answer.put(bestDelivery, products);
            }
            deliveryOccurrences.remove(bestDelivery);
        }

        return answer;
    }

    public String getBestDelivery(Map<String, Integer> deliveryOccurrences) {
        String bestDelivery = null;
        int maxOccurrences = Integer.MIN_VALUE;

        for (Map.Entry<String, Integer> entry : deliveryOccurrences.entrySet()) {
            String delivery = entry.getKey();
            int occurrences = entry.getValue();

            if (occurrences > maxOccurrences) {
                maxOccurrences = occurrences;
                bestDelivery = delivery;
            }
        }

        return bestDelivery;
    }

    public List<String> getDeliveryMethods(String nameOfProduct) {
        List<String> deliveryOptions = new ArrayList<>();

        try (FileReader fileReader = new FileReader(absolutePathToConfigFile)) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(fileReader);
            JSONObject jsonObject = (JSONObject) obj;
            Object deliveryOpt = jsonObject.get(nameOfProduct);
            if (deliveryOpt instanceof List) {
                JSONArray deliveryArray = (JSONArray) deliveryOpt;
                for (Object option : deliveryArray) {
                    deliveryOptions.add(option.toString());
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found " + e.getMessage());
            invalidInput = true;
        } catch (IOException e) {
            logger.error("Error reading a file " + e.getMessage());
            invalidInput = true;
        } catch (ParseException e) {
            logger.error("Error parsing JSON " + e.getMessage());
            invalidInput = true;
        }

        return deliveryOptions;
    }
}


