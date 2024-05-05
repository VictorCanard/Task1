import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.min;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();

        String prevName = s.next();  // Read user input

        HashMap<Character, ArrayList<Character>> rules = new HashMap<>();

        String[] words = new String[n];
        words[0] = prevName;

        for (int i = 1; i < n; i++) {
            String currName = s.next();
            words[i] = currName;

            get_rules(rules, prevName, currName);

            prevName = currName;

        }

        //add all rules by transitivity
        HashMap<Character, ArrayList<Character>> new_rules = new HashMap<>();

        for (Map.Entry<Character, ArrayList<Character>> entry : rules.entrySet()) {
            new_rules.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        for (Character lhs: rules.keySet()
        ) {
            for (Character c: rules.get(lhs)
            ) {
                if(rules.containsKey(c)){
                    List<Character> new_rhs = rules.get(c);

                    if(new_rhs.contains(lhs)){ //check no 2 contradicting rules
                        System.out.println("Impossible");
                        return;
                    }

                    new_rules.get(lhs).addAll(new_rhs);
                }

                //rhs.addAll(new_rhs);
            }
        }
        //System.out.println(new_rules);


        List<Character> alph = new LinkedList<>();
        boolean[] already_added = new boolean[26];
        // make alph using preced values as list of value element

        // Sorting the map entries by list size in descending order
        List<Map.Entry<Character, ArrayList<Character>>> sortedEntries = new_rules.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()))
                .collect(Collectors.toList());

        // Iterating over the sorted entries
        for (Map.Entry<Character, ArrayList<Character>> entry : sortedEntries) {
            alph.add(entry.getKey());
            already_added[char_to_int(entry.getKey())] = true;
        }


        // add all other chars
        for (int i = 0; i < 26; i++) {
            if(!already_added[i]){
                alph.add(int_to_char(i));
            }
        }
        //
        for (Character c: alph
        ) {
            System.out.print(c);
        }
        System.out.println();
        // EXTENSION --------------------------------------------
        // SANITY CHECK : TESTING

        List<String> wordsls = Arrays.asList(words);
        Collections.shuffle(wordsls);
        String[] w = wordsls.toArray(new String[0]);
        // Step 1: Create the precedence map based on customOrder
        Map<Character, Integer> precedenceMap = new HashMap<>();
        for (int i = 0; i < alph.size(); i++) {
            precedenceMap.put(alph.get(i), i);
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
        Arrays.sort(w, customComparator);

        // CHECK THAT RULES STILL HOLD ON NEW ORDER OF WORDS

        // Print the sorted array
        //System.out.println(Arrays.toString(words));


        }




        private static int char_to_int(char x) {
            return x - 'a';
        }
        private static char int_to_char(int x) {
            return (char) (97 + x);
        }

        private static void get_rules(HashMap<Character, ArrayList<Character>> rules, String prevName, String currName) {
            int l = min(prevName.length(), currName.length());

            for (int i = 0; i < l; i++) {
                char prev = prevName.charAt(i);
                char curr = currName.charAt(i);
                //
                if (prev != curr){
                    if(!(rules.containsKey(prev) && rules.get(prev).contains(curr)))
                        rules.merge(prev, new ArrayList<>(List.of(curr)), (o, n) -> {
                            ArrayList<Character> newls = new ArrayList<>(o);
                            newls.addAll(n);
                            return newls;});
                }
            }
        }



    public static void test() { // Runs tests
        String filePath = "src/test3.txt";

        List<Character> customOrder = WordsToAlphabet.WTA(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int n = Integer.parseInt(reader.readLine());

            String[] words = new String[n];
            for (int i = 0; i < n; i++) {
                words[i] = reader.readLine();

            }
            List<String> wordsls = Arrays.asList(words);
            Collections.shuffle(wordsls);
            String[] w = AlphabetToWords.ATW(customOrder, wordsls.toArray(new String[0]));
            // can do some testing now
        }
        catch (IOException e){
            System.err.println(e.getMessage());
        }

    }
}
