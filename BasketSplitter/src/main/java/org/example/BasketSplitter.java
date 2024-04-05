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

        return null;
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
