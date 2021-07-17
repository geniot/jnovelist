package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.view.JNovelistApplication;

import java.util.prefs.Preferences;

public class JNovelPreferences {

    public static String get(String name, String s) {
        return Preferences.userRoot().node(JNovelistApplication.class.getName()).get(name, s);
    }

    public static int getInt(String name, int i) {
        return Preferences.userRoot().node(JNovelistApplication.class.getName()).getInt(name, i);
    }

    public static void putInt(String name, int i) {
        Preferences.userRoot().node(JNovelistApplication.class.getName()).putInt(name, i);
    }

    public static void remove(String name) {
        Preferences.userRoot().node(JNovelistApplication.class.getName()).remove(name);
    }

    public static void put(String name, String path) {
        Preferences.userRoot().node(JNovelistApplication.class.getName()).put(name, path);
    }
}
