package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.JNovelistFrame;

import java.awt.event.ActionEvent;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class UnloadAction extends SaveAction {

    public UnloadAction(JNovelistFrame f) {
        super(f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        save();
        frame.statusLabel.setText("");
        frame.openDB.close();
        frame.openDB = null;
        frame.openFileName = null;
        frame.updateState();
    }
}
