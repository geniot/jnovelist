package io.github.geniot.jnovelist.actions;

import io.github.geniot.jnovelist.view.JNovelistApplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AbstractNovelistAction implements ActionListener {

    protected JNovelistApplication frame;

    public AbstractNovelistAction(JNovelistApplication f) {
        this.frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new RuntimeException("Must be overridden");
    }
}
