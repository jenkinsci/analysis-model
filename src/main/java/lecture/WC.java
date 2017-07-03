package lecture;

import java.util.HashMap;
import java.util.Map;

public class WC {
    public static void main(final String[] args) {
        new WC().count("Hallo Welt hier kommt der Zähler der die Häufigkeiten zählt");
    }

    private void count(final String s) {
        String[] words = s.split(" ");

        Map<String, Integer> counts = new HashMap<>();
        for (String word : words) {
            if (!counts.containsKey(word)) {
                counts.put(word, 1);
            }
            else {
                int value = counts.get(word) + 1;
                counts.put(word, value);
            }
        }
        System.out.println(counts);
    }
}
