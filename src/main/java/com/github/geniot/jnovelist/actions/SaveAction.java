package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.*;

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
                if (frame.dnDTabbedPane.getSelectedComponent().equals(dnd)) {
                    Constants.PROPS.put("selectedPart:" + frame.openFileName, String.valueOf(i));
                }

                Constants.PROPS.put("selectedChapter:" + i + ":" + frame.openFileName, String.valueOf(dnd.getSelectedIndex()));


                for (int k = 0; k < dnd.getTabCount(); k++) {
                    Component o = dnd.getComponentAt(k);
                    if (o instanceof ChapterEditor) {
                        ChapterEditor editor = (ChapterEditor) o;



                        try {
                            String fileDir = frame.openFileName + File.separator + (i + 1);
                            File file = new File(fileDir);
                            file.mkdirs();
                            String fileName = fileDir + File.separator + (k + 1) + ".txt";

                            Constants.PROPS.put("caretPosition:" + fileName, String.valueOf(editor.getCaretPosition()));
                            Constants.PROPS.put("verticalScrollBar:" + fileName, String.valueOf(editor.getDocumentPane().getVerticalScrollBar().getValue()));

                            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
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
