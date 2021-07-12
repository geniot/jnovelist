package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.view.JNovelistApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.prefs.Preferences;

public abstract class DesktopApplication extends JFrame {
    public static final Image ICON = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/favicon/favicon-32x32.png")).getImage();
    public Preferences preferences;

    public enum Prop {
        WIDTH, HEIGHT, POS_X, POS_Y
    }

    public DesktopApplication() {
        super();
        setIconImage(ICON);
        preferences = Preferences.userRoot().node(JNovelistApplication.class.getName());
        //Display the window.
        try {
            int width = preferences.getInt(Prop.WIDTH.name(), 600);
            int height = preferences.getInt(Prop.HEIGHT.name(), 800);
            setPreferredSize(new Dimension(width, height));
        } catch (Exception ex) {
            setPreferredSize(new Dimension(600, 800));
        }

        try {
            int posX = preferences.getInt(Prop.POS_X.name(), 50);
            int posY = preferences.getInt(Prop.POS_Y.name(), 50);
            setLocation(posX, posY);
        } catch (Exception ex) {
            setLocation(0, 0);
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onWindowClosing();
                preferences.putInt(Prop.WIDTH.name(), e.getWindow().getWidth());
                preferences.putInt(Prop.HEIGHT.name(), e.getWindow().getHeight());
                preferences.putInt(Prop.POS_X.name(), (int) e.getWindow().getLocation().getX());
                preferences.putInt(Prop.POS_Y.name(), (int) e.getWindow().getLocation().getY());
                e.getWindow().dispose();
                System.exit(0);
            }
        });
    }

    abstract public void onWindowClosing();

}
