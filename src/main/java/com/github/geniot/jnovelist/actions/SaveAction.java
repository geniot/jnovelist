package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class SaveAction extends AbstractNovelistAction  {
    private static final Logger logger = Logger.getLogger(SaveAction.class.getName());

    public SaveAction(JNovelistFrame f) {
        super(f);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        save();
        frame.saveNovel.setEnabled(false);
        frame.updateState();
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    (((ChapterEditor) ((DnDTabbedPane) frame.dnDTabbedPane.getSelectedComponent()).getSelectedComponent())).requestFocus();
//                } catch (Exception ex) {
//                    logger.log(Level.INFO, ex.getMessage());
//                }
//            }
//        });
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
                            String fileName = fileDir + File.separator + (k + 1) + ".txt";
                            File file = new File(fileName);
                            file.getParentFile().mkdirs();

                            Constants.PROPS.put("caretPosition:" + fileName, String.valueOf(editor.getCaretPosition()));
                            Constants.PROPS.put("verticalScrollBar:" + fileName, String.valueOf(editor.getDocumentPane().getVerticalScrollBar().getValue()));

                            String text = Utils.html2text(editor.getDocumentText());
                            if (StringUtils.isBlank(text)){
                                continue;
                            }

                            String newText = Utils.base64encode(text);
                            if (file.exists()) {
                                String oldText = FileUtils.readFileToString(file, "UTF-8");
                                if (Utils.textsEqual(oldText,newText)) {
                                    continue;
                                }
                            }

                            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                            try {
                                out.write(newText);
                            } finally {
                                out.close();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                //removing remaining files, obviously removed in UI
                String fileDir = frame.openFileName + File.separator + (i + 1);
                File[] ffs = new File(fileDir).listFiles();
                if (ffs.length > dnd.getTabCount()-1) {
                    Arrays.sort(ffs, Utils.FILE_NAME_NUMBER_COMPARATOR);
                    for (int l = dnd.getTabCount()-1; l < ffs.length; l++) {
                        String fileName = fileDir + File.separator + (l + 1) + ".txt";
                        File f = new File(fileName);
                        if (f.exists()) {
                            f.renameTo(new File(f.getPath()+"."+System.currentTimeMillis()));
                        }
                    }
                }
            }
        }
    }
}
