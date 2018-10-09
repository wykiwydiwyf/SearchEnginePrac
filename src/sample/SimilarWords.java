package sample;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimilarWords {

    public static HashSet<String> retrieveSimilarWords(LinkedHashMap hashMap, String[] query) {
        HashSet<String> similarWords = new HashSet();
        Map<String, Integer> map = hashMap;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            for (String Qwords: query) {
                if (distance(Qwords, entry.getKey()) == 1) {
                    similarWords.add(entry.getKey());
                }
            }
        }

        if (similarWords.isEmpty()) {  // If there are no words that have distance 1, get words with distance 2.
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                for (String Qwords: query) {
                    if (distance(Qwords, entry.getKey()) == 2) {
                        similarWords.add(entry.getKey());
                    }
                }
            }
        }
        return similarWords;
    }


    protected static int distance(String a, String b) {



        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int [] costs = new int [b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

}
