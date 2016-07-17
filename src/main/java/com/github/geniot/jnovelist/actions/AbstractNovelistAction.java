package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.JNovelistFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 30/06/15
 */
public class AbstractNovelistAction implements ActionListener {

    protected JNovelistFrame frame;

    public AbstractNovelistAction(JNovelistFrame f) {
        this.frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new RuntimeException("Must be overridden");
    }
}
