package com.github.geniot.jnovelist.model;

import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Vitaly Sazanovich
 * Email: Vitaly.Sazanovich@gmail.com
 * Date: 6/25/15
 */
public class Stats {


    public void process(int chapterNumber, String text) {
        String s = Jsoup.parse(text).text();
        String str = s.replaceAll("[!?,]", "");
        String[] words = str.split("\\s+");
        String chars =  s.replaceAll("\\s+", "");

    }

    private int sumUp(Map<Integer,Integer> m){
        int sum = 0;
        for (Integer i:m.values()){
            sum += i;
        }
        return sum;
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
//        sb.append(" ");
//        sb.append(sumUp(chaptersCharactersSpacesMap));
//        sb.append(" ");
//        sb.append(sumUp(chaptersCharactersMap));
//        sb.append(" ");
//        sb.append(sumUp(chaptersWordsMap));
        return sb.toString();
    }
}
