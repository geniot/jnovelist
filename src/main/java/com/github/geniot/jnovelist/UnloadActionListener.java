package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.Chapter;

import javax.swing.*;
import java.awt.*;
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
        int number = 0;
        frame.openDB.treeMap(Constants.COLLECTION_NOVEL).clear();
        for (int i = 0; i < frame.dnDTabbedPane.getTabCount(); i++) {
            Component c = frame.dnDTabbedPane.getComponentAt(i);
            if (c instanceof DnDTabbedPane) {
                DnDTabbedPane dnd = (DnDTabbedPane) c;
                for (int k = 0; k < dnd.getTabCount(); k++) {
                    Component o = dnd.getComponentAt(k);
                    if (o instanceof JScrollPane) {
                        JScrollPane sp = (JScrollPane) o;
                        JViewport viewport = sp.getViewport();
                        ChapterEditor editor = (ChapterEditor) viewport.getView();
                        Chapter chapter = editor.getChapter();
                        chapter.setPart(i);
                        chapter.setNumber(number);
                        chapter.setCaretPosition(editor.getCaretPosition());
                        chapter.setViewPosition(sp.getVerticalScrollBar().getValue());
                        chapter.setSelected(dnd.getSelectedComponent().equals(o));
                        frame.openDB.treeMap(Constants.COLLECTION_NOVEL).put(chapter.getNumber(), chapter);
                        ++number;
                    }
                }
            }
        }
        frame.openDB.treeMap(Constants.COLLECTION_PROPS).put(Constants.PROP_SELECTED_PART, frame.dnDTabbedPane.getSelectedIndex());

        frame.openDB.close();
        frame.openDB = null;
        frame.updateState(null);
    }
}
