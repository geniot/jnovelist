package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.*;
import com.github.geniot.jnovelist.model.Chapter;
import com.github.geniot.jnovelist.model.PersistedModel2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


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
        int number = 0;
        List<String> lines = new ArrayList<String>();
        PersistedModel2 model = new PersistedModel2();
        for (int i = 0; i < frame.dnDTabbedPane.getTabCount(); i++) {
            Component c = frame.dnDTabbedPane.getComponentAt(i);
            if (c instanceof DnDTabbedPane) {
                DnDTabbedPane dnd = (DnDTabbedPane) c;
                for (int k = 0; k < dnd.getTabCount(); k++) {
                    Component o = dnd.getComponentAt(k);
                    if (o instanceof ChapterEditor) {
                        ChapterEditor editor = (ChapterEditor) o;
                        Chapter chapter = editor.getChapter();
                        chapter.setPart(i);
                        chapter.setNumber(number);
                        chapter.setCaretPosition(editor.getDocumentPane().getEditor().getCaretPosition());
                        chapter.setViewPosition(editor.getDocumentPane().getVerticalScrollBar().getValue());
                        chapter.setSelected(dnd.getSelectedComponent().equals(o));
                        model.getNovel().add(chapter);
                        lines.add(Utils.entry2xml(chapter));
                        ++number;
                    }
                }
            }
        }
        model.setHeroes(Constants.NOTES_HEROES);
        model.setLocations(Constants.NOTES_LOCATIONS);
        model.setThings(Constants.NOTES_THINGS);
        model.setNotes(Constants.NOTES_NOTES);

        model.getProperties().setProperty(Constants.PROP_SELECTED_PART, String.valueOf(frame.dnDTabbedPane.getSelectedIndex()));

        ZipOutputStream zos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(frame.openFileName);
            zos = new ZipOutputStream(fos);
            ZipEntry zipEntry = new ZipEntry("model.ser");
            zos.putNextEntry(zipEntry);
            zos.write(Utils.serialize(model));
            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


//        Path textFile = Paths.get(frame.openFileName + ".xml");
//        try {
//            Files.write(textFile, lines, StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
