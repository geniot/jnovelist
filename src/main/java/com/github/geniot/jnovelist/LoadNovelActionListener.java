package com.github.geniot.jnovelist;

import javax.swing.*;
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
            if (frame.openDB!=null){
                frame.unloadNovel.doClick();
            }

            File selectedFile = fc.getSelectedFile();
            frame.openDB = DataAccessObject.open(selectedFile);
            frame.editorPane.setText((String) frame.openDB.treeMap("novel").get(1));
            frame.updateState(selectedFile.getName());
        }
    }
}
