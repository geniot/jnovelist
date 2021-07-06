package com.github.geniot.jnovelist.model;

import com.github.geniot.jnovelist.Constants;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
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

    public List<Chapter> getChapters(String actionCommand) {
        if (actionCommand.equals(Constants.HEROES_NOVEL_ACTION_COMMAND)) {
            return this.heroes;
        }
        if (actionCommand.equals(Constants.PLACES_NOVEL_ACTION_COMMAND)) {
            return this.places;
        }
        if (actionCommand.equals(Constants.THINGS_NOVEL_ACTION_COMMAND)) {
            return this.things;
        }
        if (actionCommand.equals(Constants.NOTES_NOVEL_ACTION_COMMAND)) {
            return this.notes;
        }
        if (actionCommand.equals(Constants.IMAGES_NOVEL_ACTION_COMMAND)) {
            return this.images;
        }
        throw new RuntimeException("Unidentified action command: " + actionCommand);
    }

    public void setChapters(List<Chapter> chapters, String actionCommand) {
        if (actionCommand.equals(Constants.HEROES_NOVEL_ACTION_COMMAND)) {
            this.heroes = chapters;
            return;
        }
        if (actionCommand.equals(Constants.PLACES_NOVEL_ACTION_COMMAND)) {
            this.places = chapters;
            return;
        }
        if (actionCommand.equals(Constants.THINGS_NOVEL_ACTION_COMMAND)) {
            this.things = chapters;
            return;
        }
        if (actionCommand.equals(Constants.NOTES_NOVEL_ACTION_COMMAND)) {
            this.notes = chapters;
            return;
        }
        if (actionCommand.equals(Constants.IMAGES_NOVEL_ACTION_COMMAND)) {
            this.images = chapters;
            return;
        }
        throw new RuntimeException("Unidentified action command: " + actionCommand);
    }
}
