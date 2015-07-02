package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.ChapterEditor;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;
import com.github.geniot.jnovelist.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class SaveAction extends AbstractNovelistAction implements ActionListener {
    private static final Logger logger = Logger.getLogger(SaveAction.class.getName());

    public SaveAction(JNovelistFrame f) {
        super(f);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        save();
//        frame.saveNovel.setEnabled(false);
        frame.updateState();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    (((ChapterEditor) ((DnDTabbedPane) frame.dnDTabbedPane.getSelectedComponent()).getSelectedComponent())).requestFocus();
                } catch (Exception ex) {
                    logger.log(Level.INFO, ex.getMessage());
                }
            }
        });
    }

    protected void save() {
        for (int i = 0; i < frame.dnDTabbedPane.getTabCount(); i++) {
            Component c = frame.dnDTabbedPane.getComponentAt(i);
            if (c instanceof DnDTabbedPane) {
                DnDTabbedPane dnd = (DnDTabbedPane) c;
                for (int k = 0; k < dnd.getTabCount(); k++) {
                    Component o = dnd.getComponentAt(k);
                    if (o instanceof ChapterEditor) {
                        ChapterEditor editor = (ChapterEditor) o;
                        try {
                            String fileDir = frame.openFileName + File.separator + (i + 1);
                            File file = new File(fileDir);
                            file.mkdirs();
                            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDir + File.separator + (k + 1) + ".txt"), "UTF-8"));
                            try {
                                out.write(Utils.html2text(editor.getDocumentText()));
                            } finally {
                                out.close();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
