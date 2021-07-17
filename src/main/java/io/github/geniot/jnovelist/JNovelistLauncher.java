package io.github.geniot.jnovelist;


import io.github.geniot.jnovelist.view.JNovelistApplication;

import javax.swing.*;

public class JNovelistLauncher {
    public static void main(final String[] args) {

        SwingUtilities.invokeLater(() -> {
            JNovelistApplication application = new JNovelistApplication();
            Utils.setLAF(JNovelPreferences.get(ChapterEditor.Prop.PROP_LAF.name(), "Luna"), application);
            application.setVisible(true);
        });
    }
}
