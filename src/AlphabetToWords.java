import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AlphabetToWords { // TO TEST THAT WTA is working correctly.
    public static String[] ATW(List<Character> customOrder, String[] words) {

        // Step 1: Create the precedence map based on customOrder
        Map<Character, Integer> precedenceMap = new HashMap<>();
        for (int i = 0; i < customOrder.size(); i++) {
            precedenceMap.put(customOrder.get(i), i);
        }

        // Step 2: Define the custom comparator
        Comparator<String> customComparator = (word1, word2) -> {
            int minLength = Math.min(word1.length(), word2.length());
            for (int i = 0; i < minLength; i++) {
                char c1 = word1.charAt(i);
                char c2 = word2.charAt(i);
                if (c1 != c2) {
                    return Integer.compare(precedenceMap.get(c1), precedenceMap.get(c2));
                }
            }
            return Integer.compare(word1.length(), word2.length());
        };

        // Step 3: Sort the array using the custom comparator
        Arrays.sort(words, customComparator);

        // Print the sorted array
        System.out.println(Arrays.toString(words));
        return words;



    }
}
