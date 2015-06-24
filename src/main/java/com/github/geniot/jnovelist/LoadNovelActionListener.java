package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.Chapter;

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
public class LoadNovelActionListener implements ActionListener {
    private JNovelistFrame frame;

    public LoadNovelActionListener(JNovelistFrame f) {
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
            frame.openDB = DataAccessObject.open(selectedFile);

            frame.dnDTabbedPane = new DnDTabbedPane(DnDTabbedPane.DECIMAL_TO_ROMAN);
            frame.getContentPane().add(frame.dnDTabbedPane, BorderLayout.CENTER);

            int chaptersCount = frame.openDB.treeMap(Constants.COLLECTION_NOVEL).size();
            int currentPart = -1;
            int selectedIndex = 0;
            for (int i = 0; i < chaptersCount; i++) {
                Chapter chapter = (Chapter) frame.openDB.treeMap(Constants.COLLECTION_NOVEL).get(i);
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

            frame.updateState(selectedFile.getName());
            frame.validate();
            frame.repaint();
        }
    }
}
