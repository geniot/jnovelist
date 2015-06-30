package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.NovelNote;

import java.util.*;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class Constants {
    public static final String PROPS_FILE_NAME = "jnovelist.properties";

    public static final String LOAD_NOVEL_ACTION_COMMAND = "LOAD_NOVEL_ACTION_COMMAND";
    public static final String UNLOAD_NOVEL_ACTION_COMMAND = "UNLOAD_NOVEL_ACTION_COMMAND";
    public static final String HEROES_NOVEL_ACTION_COMMAND = "HEROES_NOVEL_ACTION_COMMAND";
    public static final String PLACES_NOVEL_ACTION_COMMAND = "PLACES_NOVEL_ACTION_COMMAND";
    public static final String THINGS_NOVEL_ACTION_COMMAND = "THINGS_NOVEL_ACTION_COMMAND";
    public static final String NOTES_NOVEL_ACTION_COMMAND = "NOTES_NOVEL_ACTION_COMMAND";

    public static final String SAVE_NOVEL_ACTION_COMMAND = "SAVE_NOVEL_ACTION_COMMAND";

    public static final String COLLECTION_NOVEL = "COLLECTION_NOVEL";
    public static final String COLLECTION_PROPS = "COLLECTION_PROPS";

    public static final String PROP_SELECTED_PART = "PROP_SELECTED_PART";
    public static final String PROP_LAST_OPEN_FILE = "PROP_LAST_OPEN_FILE";
    public static final String PROP_WIDTH = "PROP_WIDTH";
    public static final String PROP_HEIGHT = "PROP_HEIGHT";
    public static final String PROP_POS_X = "PROP_POS_X";
    public static final String PROP_POS_Y = "PROP_POS_Y";
    public static final String PROP_LAST_OPEN_DIR = "PROP_LAST_OPEN_DIR";

    public static final String EMPTY_DOC = "<html><head><style>body {font-family:verdana; margin: 15px;}</style></head><body><p></p></body></html>";

    public static Properties PROPS = new Properties();

    public static final Map<String, String> VARS;

    static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put(HEROES_NOVEL_ACTION_COMMAND, "Characters");
        aMap.put(PLACES_NOVEL_ACTION_COMMAND, "Places");
        aMap.put(THINGS_NOVEL_ACTION_COMMAND, "Things");
        aMap.put(NOTES_NOVEL_ACTION_COMMAND, "Notes");
        VARS = Collections.unmodifiableMap(aMap);
    }

    public static List<NovelNote> NOTES_HEROES = new ArrayList<NovelNote>();
    public static List<NovelNote> NOTES_LOCATIONS = new ArrayList<NovelNote>();
    public static List<NovelNote> NOTES_THINGS = new ArrayList<NovelNote>();
    public static List<NovelNote> NOTES_NOTES = new ArrayList<NovelNote>();

    public static List<NovelNote> getNotesByCommand(String actionCommand) {
        if (actionCommand.equals(HEROES_NOVEL_ACTION_COMMAND)) {
            return NOTES_HEROES;
        } else if (actionCommand.equals(PLACES_NOVEL_ACTION_COMMAND)) {
            return NOTES_LOCATIONS;
        } else if (actionCommand.equals(THINGS_NOVEL_ACTION_COMMAND)) {
            return NOTES_THINGS;
        } else if (actionCommand.equals(NOTES_NOVEL_ACTION_COMMAND)) {
            return NOTES_NOTES;
        } else {
            return new ArrayList<NovelNote>();
        }
    }
}
