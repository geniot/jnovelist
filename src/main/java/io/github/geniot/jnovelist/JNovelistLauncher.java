package io.github.geniot.jnovelist;


import javax.swing.*;
import java.util.prefs.Preferences;

public class JNovelistLauncher {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JNovelistApplication application = new JNovelistApplication();
                Preferences preferences = Preferences.userRoot().node(JNovelistApplication.class.getName());
                String laf = preferences.get(ChapterEditor.Prop.PROP_LAF.name(), "Luna");
                Utils.setLAF(laf, application);
                application.setVisible(true);
            }
        });
    }
}
