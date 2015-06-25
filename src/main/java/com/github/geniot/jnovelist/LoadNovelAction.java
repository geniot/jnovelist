package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.Chapter;
import com.github.geniot.jnovelist.model.Stats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class LoadNovelAction implements ActionListener {
    private JNovelistFrame frame;

    public LoadNovelAction(JNovelistFrame f) {
        this.frame = f;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (frame.openDB != null) {
                frame.unloadNovel.doClick();
            }

            File selectedFile = fc.getSelectedFile();
            loadNovel(frame, selectedFile);
        }
    }

    public static void loadNovel(JNovelistFrame frame, File selectedFile) {
        frame.openDB = DataAccessObject.open(selectedFile);
        frame.openFileName = selectedFile.getAbsolutePath();
        frame.dnDTabbedPane = new DnDTabbedPane(DnDTabbedPane.DECIMAL_TO_ROMAN);
        frame.getContentPane().add(frame.dnDTabbedPane, BorderLayout.CENTER);

        int chaptersCount = frame.openDB.treeMap(Constants.COLLECTION_NOVEL).size();
        int currentPart = -1;
        int selectedIndex = 0;
        Stats stats = new Stats();

        for (int i = 0; i < chaptersCount; i++) {
            Chapter chapter = (Chapter) frame.openDB.treeMap(Constants.COLLECTION_NOVEL).get(i);
            if (chapter == null) {
                continue;
            }
            stats.process(chapter.getNumber(), chapter.getText());
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


        Object o = frame.openDB.treeMap(Constants.COLLECTION_PROPS).get(Constants.PROP_SELECTED_PART);
        frame.dnDTabbedPane.setSelectedIndex(o == null ? 0 : (Integer) o);
        frame.stats = stats;

        frame.updateState();
        frame.validate();
        frame.repaint();
    }
}
