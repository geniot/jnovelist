package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.DataAccessObject;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;
import com.github.geniot.jnovelist.model.Chapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class LoadNovelAction implements ActionListener {
    private static final Logger logger = Logger.getLogger(LoadNovelAction.class.getName());
    private JNovelistFrame frame;

    public LoadNovelAction(JNovelistFrame f) {
        this.frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc;
        if (Constants.PROPS.containsKey(Constants.PROP_LAST_OPEN_DIR)) {
            try {
                fc = new JFileChooser(Constants.PROPS.getProperty(Constants.PROP_LAST_OPEN_DIR));
            } catch (Exception ex) {
                logger.log(Level.WARNING, ex.getMessage());
                fc = new JFileChooser();
            }
        } else {
            fc = new JFileChooser();
        }

        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (frame.openDB != null) {
                frame.unloadNovel.doClick();
            }

            File selectedFile = fc.getSelectedFile();
            Constants.PROPS.setProperty(Constants.PROP_LAST_OPEN_DIR, selectedFile.getPath());
            loadNovel(frame, selectedFile);
        }
    }

    public static void loadNovel(JNovelistFrame frame, File selectedFile) {
        try {
            frame.openDB = DataAccessObject.open(selectedFile);
            frame.openFileName = selectedFile.getAbsolutePath();
            frame.dnDTabbedPane = new DnDTabbedPane(DnDTabbedPane.DECIMAL_TO_ROMAN);
            frame.getContentPane().add(frame.dnDTabbedPane, BorderLayout.CENTER);

            int chaptersCount = frame.openDB.treeMap(Constants.COLLECTION_NOVEL).size();
            int currentPart = -1;
            int selectedIndex = 0;

            for (int i = 0; i < chaptersCount; i++) {
                Chapter chapter = (Chapter) frame.openDB.treeMap(Constants.COLLECTION_NOVEL).get(i);
                if (chapter == null) {
                    continue;
                }
                if (chapter.getPart() != currentPart) {
                    frame.dnDTabbedPane.addNewTab(null);
                    currentPart = chapter.getPart();
                }
                DnDTabbedPane partTab = (DnDTabbedPane) frame.dnDTabbedPane.getComponentAt(currentPart);
                partTab.addNewTab(chapter);
                if (chapter.isSelected()) {
                    selectedIndex = partTab.getTabCount() - 2;
                }
                partTab.setSelectedIndex(selectedIndex);
            }

            if (chaptersCount == 0) {
                frame.dnDTabbedPane.addNewTab(null);
                DnDTabbedPane partTab = (DnDTabbedPane) frame.dnDTabbedPane.getComponentAt(0);
                partTab.addNewTab(null);
            }

            Object o = frame.openDB.treeMap(Constants.COLLECTION_PROPS).get(Constants.PROP_SELECTED_PART);
            frame.dnDTabbedPane.setSelectedIndex(o == null ? 0 : (Integer) o);
            frame.updateStatus();
            frame.updateState();
            frame.validate();
            frame.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
