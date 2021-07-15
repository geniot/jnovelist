package io.github.geniot.jnovelist.model;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 15/06/17
 */
public class Chapter implements Serializable {
    private String[] lines = new String[]{};

    public String[] getLines() {
        return lines;
    }

    public void setLines(String[] lines) {
        this.lines = lines;
    }

    public int sizeCharacters() {
        int sum = 0;
        for (String line : getLines()) {
            sum += line.length();
        }
        return sum;
    }

    public int sizeWords() {
        int sum = 0;
        for (String line : getLines()) {
            sum += new StringTokenizer(line).countTokens();
        }
        return sum;
    }
}
