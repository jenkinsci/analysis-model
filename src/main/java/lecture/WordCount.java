package lecture;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Shows several word count implementations.
 *
 * @author Ullrich Hafner
 */
public class WordCount {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        new WordCount().wc(input);
        new WordCount().wcJava7(input);
        new WordCount().wcGuava(input);
    }

    private void wcGuava(final String input) {
        Multimap<String, Integer> wordCounts = HashMultimap.create();
        String[] words = input.split(" ");
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            wordCounts.put(word, i);
        }
        System.out.println(wordCounts);
    }

    private void wcJava7(final String input) {
        Map<String, Integer> wordCounts = new HashMap<>();
        for (String word : input.split(" ")) {
            int counter;
            if (wordCounts.containsKey(word)) {
                counter = wordCounts.get(word) + 1;
            }
            else {
                counter = 1;
            }
            wordCounts.put(word, counter);
        }
        System.out.println(wordCounts);
    }

    private void wc(final String input) {
        Map<String, Integer> wordCounts = new HashMap<>();
        for (String word : input.split(" ")) {
            wordCounts.putIfAbsent(word, 0);
            int counter = wordCounts.get(word);
            wordCounts.put(word, counter + 1);
        }
        System.out.println(wordCounts);
    }
}
