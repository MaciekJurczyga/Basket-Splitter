package org.example;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class BasketSplitter {
    public final String absolutePathToConfigFile;

    public BasketSplitter(String absolutePathToConfigFile){
        this.absolutePathToConfigFile = absolutePathToConfigFile;
    }


    public Map<String, List<String>> split(List<String> items){
        Map<String, Integer> deliveryOccurrences = new HashMap<>();
        Map<String, List<String>> answer = new HashMap<>();
        for(String item:items){
            List<String> deliveryOptions = getDeliveryMethods(item);
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
            List<String> itemsCopy = new ArrayList<>(items);
            for(String item:itemsCopy){
                products.clear();
                if(getDeliveryMethods(item).contains(bestDelivery)){
                    if(answer.containsKey(bestDelivery)){
                        products = answer.get(bestDelivery);
                        products.add(item);
                        answer.put(bestDelivery, products);
                    }
                    else{
                        products.add(item);
                        answer.put(bestDelivery, products);
                    }
                    items.remove(item);
                    for(String option:getDeliveryMethods(item)){
                        deliveryOccurrences.put(option, deliveryOccurrences.get(option)-1);
                    }

                }
            }
            deliveryOccurrences.remove(bestDelivery);
        }


        return answer;
    }

    private String getBestDelivery(Map<String, Integer> deliveryOccurrences) {
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


    public List<String> getDeliveryMethods(String nameOfProduct){
        JSONParser parser = new JSONParser();
        List<String> deliveryOptions = new ArrayList<>();
        try{
            Object obj = parser.parse(new FileReader(absolutePathToConfigFile));
            JSONObject jsonObject = (JSONObject)obj;
            Object deliveryOpt = jsonObject.get(nameOfProduct);
            if(deliveryOpt instanceof List){
                JSONArray deliveryArray = (JSONArray) deliveryOpt;
                for(Object option : deliveryArray){
                    deliveryOptions.add(option.toString());
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return deliveryOptions;
    }

}
