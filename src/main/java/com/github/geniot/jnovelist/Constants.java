package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.PersistedModel;

import java.util.Properties;

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

}
