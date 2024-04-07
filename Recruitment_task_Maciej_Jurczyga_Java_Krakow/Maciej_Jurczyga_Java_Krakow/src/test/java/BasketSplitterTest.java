import static org.junit.Assert.assertEquals;

import java.util.*;

import com.ocado.basket.BasketSplitter;
import org.junit.Before;
import org.junit.Test;

/// !!! tests are described in readme !!! ///
public class BasketSplitterTest {
    private BasketSplitter basketSplitter;

    @Before
    public void setUp(){
        String absolutePathToConfigFile = "src/main/resources/config.json";
        basketSplitter = new BasketSplitter(absolutePathToConfigFile);
    }

    @Test
    public void testGetDeliveryOptions(){
        List<String> deliveryOptions = basketSplitter.getDeliveryMethods("Cocoa Butter");
        List<String> expectedOptions = Arrays.asList("Next day shipping", "Mailbox delivery", "In-store pick-up", "Parcel locker", "Courier", "Same day delivery");
        assertEquals(deliveryOptions, expectedOptions);
    }

    @Test
    public void testGetBestDelivery(){
        Map<String, Integer> occurrences = new HashMap<>();
        occurrences.put("Next day shipping", 6);
        occurrences.put("Mailbox delivery", 12);
        occurrences.put("Courier", 4);

        String bestDelivery = basketSplitter.getBestDelivery(occurrences);
        assertEquals("Mailbox delivery", bestDelivery);
    }

    @Test
    public void testIncorrectPath(){
       BasketSplitter basketSplitter = new BasketSplitter("/incorrect/path/to/file");
       List<String> deliveryMethods = basketSplitter.getDeliveryMethods("Bread - Flat Bread");
       assertEquals(0, deliveryMethods.size());
    }

    @Test
    public void testEmptyInput(){
        List<String> items = new ArrayList<>();
        assertEquals(0, basketSplitter.split(items).size());
    }

    @Test
    public void testNonExistingProduct(){
        List<String> deliveryOptions = basketSplitter.getDeliveryMethods("Non existing product");
        assertEquals(0, deliveryOptions.size());
    }

    @Test
    public void testNonExistingProduct2(){
        List<String> items = new ArrayList<>(Arrays.asList("non-existing-item","Fond - Chocolate", "Longan", "Appetizer - Escargot Puff", "Carbonated Water - Raspberry" , "Energy Drink - Redbull 355ml"));
        List<String> expectedItems = new ArrayList<>(Arrays.asList("Fond - Chocolate", "Longan", "Appetizer - Escargot Puff", "Carbonated Water - Raspberry" , "Energy Drink - Redbull 355ml"));
        Map<String, List<String>> ans = basketSplitter.split(items);
        assertEquals(expectedItems, ans.get("Express Collection"));
    }

    @Test
    public void testSplit1(){
        List<String> items = new ArrayList<>(Arrays.asList("Fond - Chocolate", "Longan", "Appetizer - Escargot Puff", "Carbonated Water - Raspberry" , "Energy Drink - Redbull 355ml"));
        List<String> copyItems = new ArrayList<>(Arrays.asList("Fond - Chocolate", "Longan", "Appetizer - Escargot Puff", "Carbonated Water - Raspberry" , "Energy Drink - Redbull 355ml"));
        Map<String, List<String>> ans = basketSplitter.split(items);
        assertEquals(copyItems, ans.get("Express Collection"));
    }

    @Test
    public void testSplit2(){
        List<String> items = new ArrayList<>( Arrays.asList("Sauce - Mint", "Numi - Assorted Teas", "Garlic - Peeled",
                "Cake - Miini Cheesecake Cherry", "Fond - Chocolate", "Chocolate - Unsweetened",
                "Nut - Almond, Blanched, Whole", "Haggis", "Mushroom - Porcini Frozen",
                "Longan", "Bag Clear 10 Lb", "Nantucket - Pomegranate Pear", "Puree - Strawberry",
                "Apples - Spartan", "Cabbage - Nappa", "Bagel - Whole White Sesame",
                "Tea - Apple Green Tea"));

        String expected = """
                Key: Same day delivery
                  Value: Sauce - Mint
                  Value: Numi - Assorted Teas
                  Value: Garlic - Peeled

                Key: Courier
                  Value: Cake - Miini Cheesecake Cherry

                Key: Express Collection
                  Value: Fond - Chocolate
                  Value: Chocolate - Unsweetened
                  Value: Nut - Almond, Blanched, Whole
                  Value: Haggis
                  Value: Mushroom - Porcini Frozen
                  Value: Longan
                  Value: Bag Clear 10 Lb
                  Value: Nantucket - Pomegranate Pear
                  Value: Puree - Strawberry
                  Value: Apples - Spartan
                  Value: Cabbage - Nappa
                  Value: Bagel - Whole White Sesame
                  Value: Tea - Apple Green Tea
                  
                """;


        Map<String, List<String>> result = basketSplitter.split(items);

        StringBuilder actual = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : result.entrySet()) {
            actual.append("Key: ").append(entry.getKey()).append("\n");
            for (String value : entry.getValue()) {
                actual.append("  Value: ").append(value).append("\n");
            }
            actual.append("\n");
        }

        assertEquals(expected, actual.toString());
    }
}
