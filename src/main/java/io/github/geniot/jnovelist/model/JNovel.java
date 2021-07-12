package io.github.geniot.jnovelist.model;


import io.github.geniot.jnovelist.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 15/06/17
 */
public class JNovel implements Serializable {
    private List<Part> parts = new ArrayList<Part>();

    private List<Chapter> heroes = new ArrayList<Chapter>();
    private List<Chapter> places = new ArrayList<Chapter>();
    private List<Chapter> things = new ArrayList<Chapter>();
    private List<Chapter> notes = new ArrayList<Chapter>();
    private List<Chapter> images = new ArrayList<Chapter>();

    public JNovel() {
        parts.add(new Part());
    }

    public List<Chapter> getImages() {
        return images;
    }

    public void setImages(List<Chapter> images) {
        this.images = images;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }

    public List<Chapter> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<Chapter> heroes) {
        this.heroes = heroes;
    }

    public List<Chapter> getPlaces() {
        return places;
    }

    public void setPlaces(List<Chapter> places) {
        this.places = places;
    }

    public List<Chapter> getThings() {
        return things;
    }

    public void setThings(List<Chapter> things) {
        this.things = things;
    }

    public List<Chapter> getNotes() {
        return notes;
    }

    public void setNotes(List<Chapter> notes) {
        this.notes = notes;
    }


    public List<String> exportNovel() {
        List<String> novelLines = new ArrayList<>();
        int partsCounter = 1;
        for (Part part : parts) {
            novelLines.add(Utils.decimalIndexToRoman(partsCounter++));
            int chapterCounter = 1;
            for (Chapter chapter : part.getChapters()) {
                novelLines.add(String.valueOf(chapterCounter++));
                novelLines.addAll(Arrays.asList(chapter.getLines()));

            }
        }
        return novelLines;
    }
}
