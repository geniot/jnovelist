package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.JNovelistFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class OnExitAction extends WindowAdapter {
    private JNovelistFrame frame;

    public OnExitAction(JNovelistFrame f) {
        this.frame = f;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Constants.PROPS.setProperty(Constants.PROP_WIDTH, String.valueOf(frame.getWidth()));
        Constants.PROPS.setProperty(Constants.PROP_HEIGHT, String.valueOf(frame.getHeight()));
        Constants.PROPS.setProperty(Constants.PROP_POS_X, String.valueOf((int) frame.getLocation().getX()));
        Constants.PROPS.setProperty(Constants.PROP_POS_Y, String.valueOf((int) frame.getLocation().getY()));

        if (frame.openFileName != null) {
            Constants.PROPS.setProperty(Constants.PROP_LAST_OPEN_FILE, frame.openFileName);
            frame.unloadNovel.doClick();
        } else {
            Constants.PROPS.remove(Constants.PROP_LAST_OPEN_FILE);
        }
        try {
            FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + File.separator + Constants.PROPS_FILE_NAME);
            Constants.PROPS.store(fos, "");
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        e.getWindow().dispose();
    }
}
