package com.github.geniot.jnovelist;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
    public static final String EXPORT_NOVEL_ACTION_COMMAND = "EXPORT_NOVEL_ACTION_COMMAND";

    public static final String HEROES_NOVEL_ACTION_COMMAND = "HEROES_NOVEL_ACTION_COMMAND";
    public static final String PLACES_NOVEL_ACTION_COMMAND = "PLACES_NOVEL_ACTION_COMMAND";
    public static final String THINGS_NOVEL_ACTION_COMMAND = "THINGS_NOVEL_ACTION_COMMAND";
    public static final String NOTES_NOVEL_ACTION_COMMAND = "NOTES_NOVEL_ACTION_COMMAND";
    public static final String IMAGES_NOVEL_ACTION_COMMAND = "IMAGES_NOVEL_ACTION_COMMAND";
    public static final String STYLE_NOVEL_ACTION_COMMAND = "STYLE_NOVEL_ACTION_COMMAND";
    public static final String DICTIONARY_ACTION_COMMAND = "DICTIONARY_ACTION_COMMAND";
    public static final String INFO_ACTION_COMMAND = "INFO_ACTION_COMMAND";

    public static final String WORDS_COUNTER = "WORDS_COUNTER";
    public static final String CHARS_COUNTER = "CHARS_COUNTER";

    public static final String SAVE_NOVEL_ACTION_COMMAND = "SAVE_NOVEL_ACTION_COMMAND";

    public static final String PROP_LAST_OPEN_FILE = "PROP_LAST_OPEN_FILE";
    public static final String PROP_WIDTH = "PROP_WIDTH";
    public static final String PROP_HEIGHT = "PROP_HEIGHT";
    public static final String PROP_POS_X = "PROP_POS_X";
    public static final String PROP_POS_Y = "PROP_POS_Y";
    public static final String PROP_DIVIDER_LOCATION = "PROP_DIVIDER_LOCATION";
    public static final String PROP_LAST_OPEN_DIR = "PROP_LAST_OPEN_DIR";
    public static final String PROP_STYLE = "PROP_STYLE";
    public static final String PROP_SYNOPSIS_STYLE = "PROP_SYNOPSIS_STYLE";

    public static String HTML_DOC_START = "<html><head><style>body {\n" +
            "background-color: #EEEEEE; \n" +
            "color: #696969; \n" +
            "font-size:16pt; \n" +
            "font-family:verdana; \n" +
            "margin: 15px;\n" +
            "/* http://www.w3schools.com/cssref/css_colors.asp */\n" +
            "}</style></head><body>";
    public static final String HTML_DOC_END = "</body></html>";

    public static String HTML_SYN_DOC_START = "<html><head><style>body {\n" +
            "background-color: #EEEEEE; \n" +
            "color: #696969; \n" +
            "font-size:16pt; \n" +
            "font-family:verdana; \n" +
            "margin: 15px;\n" +
            "/* http://www.w3schools.com/cssref/css_colors.asp */\n" +
            "}</style></head><body>";
    public static final String HTML_SYN_DOC_END = "</body></html>";


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
