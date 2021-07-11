package io.github.geniot.jnovelist.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 15/06/17
 */
public class Part implements Serializable {
    private List<Chapter> chapters = new ArrayList<>();

    public Part() {
        chapters.add(new Chapter());
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
