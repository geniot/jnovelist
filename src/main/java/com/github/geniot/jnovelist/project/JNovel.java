package com.github.geniot.jnovelist.project;

import com.github.geniot.jnovelist.Constants;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 15/06/17
 */
@XmlRootElement
public class JNovel implements Serializable {
    private Chapter[] chapters = new Chapter[]{};

    private String synopsis = "";

    private Scene[] heroes = new Scene[]{};
    private Scene[] places = new Scene[]{};
    private Scene[] things = new Scene[]{};
    private Scene[] notes = new Scene[]{};

    public Chapter[] getChapters() {
        return chapters;
    }

    @XmlElement
    public void setChapters(Chapter[] chapters) {
        this.chapters = chapters;
    }


    public Scene[] getHeroes() {
        return heroes;
    }

    @XmlElement
    public void setHeroes(Scene[] heroes) {
        this.heroes = heroes;
    }

    public Scene[] getPlaces() {
        return places;
    }

    @XmlElement
    public void setPlaces(Scene[] places) {
        this.places = places;
    }

    public Scene[] getThings() {
        return things;
    }

    @XmlElement
    public void setThings(Scene[] things) {
        this.things = things;
    }

    public Scene[] getNotes() {
        return notes;
    }

    @XmlElement
    public void setNotes(Scene[] notes) {
        this.notes = notes;
    }

    public String getSynopsis() {
        return synopsis;
    }

    @XmlElement
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Scene[] getScenes(String actionCommand) {
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
        throw new RuntimeException("Unidentified action command: " + actionCommand);
    }

    public void setScenes(Scene[] scenes, String actionCommand) {
        if (actionCommand.equals(Constants.HEROES_NOVEL_ACTION_COMMAND)) {
            this.heroes = scenes;
            return;
        }
        if (actionCommand.equals(Constants.PLACES_NOVEL_ACTION_COMMAND)) {
            this.places = scenes;
            return;
        }
        if (actionCommand.equals(Constants.THINGS_NOVEL_ACTION_COMMAND)) {
            this.things = scenes;
            return;
        }
        if (actionCommand.equals(Constants.NOTES_NOVEL_ACTION_COMMAND)) {
            this.notes = scenes;
            return;
        }
        throw new RuntimeException("Unidentified action command: " + actionCommand);
    }
}
