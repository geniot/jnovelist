package io.github.geniot.jnovelist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class DesktopApplication extends JFrame {
    public static final Image ICON = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/favicon/favicon-32x32.png")).getImage();

    public DesktopApplication() {
        super();
        setIconImage(ICON);
        //Display the window.
        try {
            int width = JNovelPreferences.getInt(Prop.WIDTH.name(), 600);
            int height = JNovelPreferences.getInt(Prop.HEIGHT.name(), 600);
            setPreferredSize(new Dimension(width, height));
        } catch (Exception ex) {
            setPreferredSize(new Dimension(600, 600));
        }

        try {
            int posX = JNovelPreferences.getInt(Prop.POS_X.name(), 50);
            int posY = JNovelPreferences.getInt(Prop.POS_Y.name(), 50);
            setLocation(posX, posY);
        } catch (Exception ex) {
            setLocation(0, 0);
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                onWindowClosing();
                JNovelPreferences.putInt(Prop.WIDTH.name(), e.getWindow().getWidth());
                JNovelPreferences.putInt(Prop.HEIGHT.name(), e.getWindow().getHeight());
                JNovelPreferences.putInt(Prop.POS_X.name(), (int) e.getWindow().getLocation().getX());
                JNovelPreferences.putInt(Prop.POS_Y.name(), (int) e.getWindow().getLocation().getY());
                e.getWindow().dispose();
                System.exit(0);
            }
        });
    }

    abstract public void onWindowClosing();

    public enum Prop {
        WIDTH, HEIGHT, POS_X, POS_Y
    }

}
