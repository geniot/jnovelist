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
        frame.openDB.treeMap(Constants.COLLECTION_NOVEL).put(1, frame.editorPane.getText());
        frame.openDB.treeMap(Constants.COLLECTION_PROPS).put(Constants.PROP_CARET_POS, frame.editorPane.getCaretPosition());
        frame.openDB.treeMap(Constants.COLLECTION_PROPS).put(Constants.PROP_VIEW_POS, frame.scrollPane.getVerticalScrollBar().getValue());
        frame.openDB.close();
        frame.openDB = null;
        frame.updateState(null);
    }
}
