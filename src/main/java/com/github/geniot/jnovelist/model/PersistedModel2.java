package com.github.geniot.jnovelist.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 30/06/15
 */
public class PersistedModel2 extends PersistedModel {
    private List<NovelNote> heroes = new ArrayList<NovelNote>();
    private List<NovelNote> locations = new ArrayList<NovelNote>();
    private List<NovelNote> things = new ArrayList<NovelNote>();
    private List<NovelNote> notes = new ArrayList<NovelNote>();

    public PersistedModel2() {
    }

    public PersistedModel2(PersistedModel pm) {
        setNovel(pm.getNovel());
        setProperties(pm.getProperties());
    }


    public List<NovelNote> getHeroes() {
        return heroes;
    }

    public void setHeroes(List<NovelNote> heroes) {
        this.heroes = heroes;
    }

    public List<NovelNote> getLocations() {
        return locations;
    }

    public void setLocations(List<NovelNote> locations) {
        this.locations = locations;
    }

    public List<NovelNote> getThings() {
        return things;
    }

    public void setThings(List<NovelNote> things) {
        this.things = things;
    }

    public List<NovelNote> getNotes() {
        return notes;
    }

    public void setNotes(List<NovelNote> notes) {
        this.notes = notes;
    }
}
