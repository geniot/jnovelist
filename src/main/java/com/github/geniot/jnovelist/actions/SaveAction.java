package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.*;
import com.github.geniot.jnovelist.model.Chapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class SaveAction implements ActionListener {
    private static final Logger logger = Logger.getLogger(SaveAction.class.getName());

    protected JNovelistFrame frame;

    public SaveAction(JNovelistFrame f) {
        this.frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        save();
        frame.saveNovel.setEnabled(false);
        frame.updateState();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    (((JScrollPane) ((DnDTabbedPane) frame.dnDTabbedPane.getSelectedComponent()).getSelectedComponent()).getViewport().getView()).requestFocus();
                } catch (Exception ex) {
                    logger.log(Level.INFO, ex.getMessage());
                }
            }
        });
    }

    protected void save() {
        int number = 0;
        List<String> lines = new ArrayList<String>();
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
                        lines.add(Utils.entry2xml(chapter));
                        ++number;
                    }
                }
            }
        }
        frame.openDB.treeMap(Constants.COLLECTION_PROPS).put(Constants.PROP_SELECTED_PART, frame.dnDTabbedPane.getSelectedIndex());
        frame.openDB.commit();

        Path textFile = Paths.get(frame.openFileName + ".xml");
        try {
            Files.write(textFile, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
