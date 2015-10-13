package com.github.geniot.jnovelist;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 14/09/15
 */
public class Levenshtein {
    public static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int[] costs = new int[b.length() + 1];
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

    public static void main(String[] args) {
        String[] data = {"kitten", "sitting", "saturday", "sunday", "rosettacode", "raisethysword"};
        System.out.println(distance("word", "word"));
        System.out.println(distance("word", "w2rd"));
        System.out.println(distance("word", "words"));
        System.out.println(distance("word", "words2"));
        System.out.println(distance("word", "words22"));

    }
}
