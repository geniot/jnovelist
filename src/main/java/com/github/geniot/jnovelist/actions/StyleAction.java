package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.JNovelistFrame;
import com.github.geniot.jnovelist.view.PreferencesDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 20/07/16
 */
public class StyleAction extends AbstractNovelistAction {
    PreferencesDialog dialog;
    String actionCommand;

    public StyleAction(JNovelistFrame f) {
        super(f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.actionCommand = e.getActionCommand();
        dialog = new PreferencesDialog(frame);
        dialog.setVisible(true);
    }


}


