package com.github.geniot.jnovelist;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import static java.util.ResourceBundle.getBundle;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class Constants {
    public static final String PROPS_FILE_NAME = "jnovelist.properties";
    public static final Image ICON = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/favicon/favicon-32x32.png")).getImage();

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

    public enum PropKey {
        PROP_LAST_OPEN_FILE,
        PROP_WIDTH,
        PROP_HEIGHT,
        PROP_POS_X,
        PROP_POS_Y,
        PROP_LAST_OPEN_DIR,
        PROP_BG_COLOR,
        PROP_TXT_COLOR,
        PROP_FONT_FACE,
        PROP_FONT_SIZE,
        PROP_MARGIN
    }

    public static String HTML_DOC_START() {
        String bgColor = PROPS.getProperty(PropKey.PROP_BG_COLOR.name());
        String txtColor = PROPS.getProperty(PropKey.PROP_TXT_COLOR.name());
        String fontFace = PROPS.getProperty(PropKey.PROP_FONT_FACE.name());
        int fontSize = Integer.parseInt(PROPS.getProperty(PropKey.PROP_FONT_SIZE.name()));
        int margin = Integer.parseInt(PROPS.getProperty(PropKey.PROP_MARGIN.name()));

        return "<html><head><style>body {\n" +
                "background-color: " + bgColor + "; \n" +
                "color: " + txtColor + "; \n" +
                "font-size:" + fontSize + "pt; \n" +
                "font-family:" + fontFace + "; \n" +
                "margin: " + margin + "px;\n" +
                "}</style></head><body>";
    }

    public static final String HTML_DOC_END = "</body></html>";

    public static Properties PROPS = new Properties();
    public static ResourceBundle RES = getBundle("ResourceBundle");

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

    public static final String LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. \n" +
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. \n" +
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. \n" +
            "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    public static final String DEFAULT_LAF = "com.jtattoo.plaf.smart.SmartLookAndFeel";
    public static String LAF_PREFIX = "com.jtattoo.plaf.";
    public static String LAF_SUFFIX = "LookAndFeel";

    static {
        //classpath properties
        try {
            Constants.PROPS.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(Constants.PROPS_FILE_NAME));
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't load properties file from the classpath.");
        }
        try {
            Constants.PROPS.load(new FileInputStream(System.getProperty("user.home") + File.separator + Constants.PROPS_FILE_NAME));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
