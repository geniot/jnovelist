package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.view.JNovelistApplication;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class StatusUpdater implements DocumentListener {
    private JNovelistApplication frame;

    public StatusUpdater(JNovelistApplication f) {
        this.frame = f;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        frame.updateStatus();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        frame.updateStatus();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        frame.updateStatus();
    }
}
