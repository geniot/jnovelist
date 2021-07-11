package io.github.geniot.jnovelist.model;

import java.io.Serializable;

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
}
