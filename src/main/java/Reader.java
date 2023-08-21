import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Reader {
    public static void main(String[] args) {

        String path = "/Users/destany/Documents/Projects/HurtLocker/src/main/resources/RawData.txt";
        String line = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Map<String, Map<String, Integer>> dataMap = new HashMap<>();

            while ((line = br.readLine()) != null) {
                String[] values = line.split("##");

                for (String pair : values) {
                    parseValues(pair, dataMap);
                }
            }

            br.close();
            printFormattedOutput(dataMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String parseValues(String pair, Map<String, Map<String, Integer>> dataMap) {
        String[] elements = pair.split(";");

        String name = "";
        String price = "";

        for (String element : elements) {
            String[] keyValue = element.split("[:@^*%]");
            if (keyValue.length == 2) {
                String key = findCorrectSpelling(keyValue[0]);
                String value = keyValue[1];

                if (key.equalsIgnoreCase("name")) {
                    name = findCorrectSpelling(value);
                } else if (key.equalsIgnoreCase("price")) {
                    price = value;
                }

                dataMap.putIfAbsent(name, new HashMap<>());
                dataMap.get(name).put("name", dataMap.get(name).getOrDefault("name", 0) + 1);
                dataMap.get(name).put("price", dataMap.get(name).getOrDefault("price", 0) + 1);
            }
        }
        return name;
    }

        private static String findCorrectSpelling(String word) {

            if (word.matches("(?i)CoOkieS|Co0kieS|COokIes")) {
                return "Cookies";
            } else if (word.matches("(?i)Milk|MiLK")) {
                return "Milk";
            } else if (word.matches("(?i)BreaD|BrEAD|")) {
                return "Bread";
            } else if (word.matches("(?i)apPles")) {
                return "Apples";
            } else if (word.matches("(?i)naMe|naME|nAMe|NAME")) {
                return "name";
            } else if (word.matches("(?i)price|pRice|priCe|prIce")) {
                return "price";
            }

            return word;
        }

        private static void printFormattedOutput(Map<String, Map<String, Integer>> dataMap) {
            for (Map.Entry<String, Map<String, Integer>> entry : dataMap.entrySet()) {
                System.out.println("name:   " + entry.getKey() + "   \t seen: " + entry.getValue().get("name") + " times");

                for (Map.Entry<String, Integer> subEntry : entry.getValue().entrySet()) {
                    if (!subEntry.getKey().equalsIgnoreCase("name")) {
                        String priceKey = subEntry.getKey();
                        String priceValue = priceKey.replace("price:", "");
                        System.out.println("price:   " + priceValue + "   \t seen: " + subEntry.getValue() + " times");
                    }
                }
                System.out.println("=============\t =============\n");
            }
        }
}






