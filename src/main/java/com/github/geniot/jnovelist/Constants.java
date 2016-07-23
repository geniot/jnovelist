package com.github.geniot.jnovelist;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.security.MessageDigest.getInstance;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class Constants {
    public static final String PROPS_FILE_NAME = "jnovelist.properties";

    public static final String LOAD_NOVEL_ACTION_COMMAND = "LOAD_NOVEL_ACTION_COMMAND";
    public static final String UNLOAD_NOVEL_ACTION_COMMAND = "UNLOAD_NOVEL_ACTION_COMMAND";
    public static final String EXPORT_NOVEL_ACTION_COMMAND = "EXPORT_NOVEL_ACTION_COMMAND";

    public static final String HEROES_NOVEL_ACTION_COMMAND = "HEROES_NOVEL_ACTION_COMMAND";
    public static final String PLACES_NOVEL_ACTION_COMMAND = "PLACES_NOVEL_ACTION_COMMAND";
    public static final String THINGS_NOVEL_ACTION_COMMAND = "THINGS_NOVEL_ACTION_COMMAND";
    public static final String NOTES_NOVEL_ACTION_COMMAND = "NOTES_NOVEL_ACTION_COMMAND";
    public static final String IMAGES_NOVEL_ACTION_COMMAND = "IMAGES_NOVEL_ACTION_COMMAND";
    public static final String DICTIONARY_ACTION_COMMAND = "DICTIONARY_ACTION_COMMAND";
    public static final String INFO_ACTION_COMMAND = "INFO_ACTION_COMMAND";

    public static final String WORDS_COUNTER = "WORDS_COUNTER";
    public static final String CHARS_COUNTER = "CHARS_COUNTER";

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

    public static final String HTML_DOC_START = "<html><head><style>body {font-family:verdana; margin: 15px;}</style></head><body>";
    public static final String HTML_DOC_END = "</body></html>";
    public static final String EMPTY_DOC = HTML_DOC_START + "<p></p>" + HTML_DOC_END;

    public static Properties PROPS = new Properties();

    public static final String HELP_FOLDER_NAME = "help";

    public static final Map<String, String> VARS;

    static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put(HEROES_NOVEL_ACTION_COMMAND, "Characters");
        aMap.put(PLACES_NOVEL_ACTION_COMMAND, "Places");
        aMap.put(THINGS_NOVEL_ACTION_COMMAND, "Things");
        aMap.put(NOTES_NOVEL_ACTION_COMMAND, "Notes");
        aMap.put(IMAGES_NOVEL_ACTION_COMMAND, "Images");
        aMap.put(DICTIONARY_ACTION_COMMAND, "Synonyms");
        aMap.put(INFO_ACTION_COMMAND, "Info");
        aMap.put(WORDS_COUNTER, "СЛВ");
        aMap.put(CHARS_COUNTER, "СМВ");
        VARS = Collections.unmodifiableMap(aMap);
    }

}
