package com.github.geniot.jnovelist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class UnloadActionListener implements ActionListener {
    private JNovelistFrame frame;

    public UnloadActionListener(JNovelistFrame f) {
        this.frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.openDB.treeMap("novel").put(1, frame.editorPane.getText());
        frame.openDB.close();
        frame.openDB = null;
        frame.updateState(null);
    }
}
