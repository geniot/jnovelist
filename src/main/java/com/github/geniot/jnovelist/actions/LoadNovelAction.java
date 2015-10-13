package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;
import com.github.geniot.jnovelist.Utils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class LoadNovelAction extends AbstractNovelistAction implements ActionListener {
    private static final Logger logger = Logger.getLogger(LoadNovelAction.class.getName());

    public LoadNovelAction(JNovelistFrame f) {
        super(f);
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

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (frame.openFileName != null) {
                frame.unloadNovel.doClick();
            }

            File selectedFile = fc.getSelectedFile();
            Constants.PROPS.setProperty(Constants.PROP_LAST_OPEN_DIR, fc.getCurrentDirectory().getPath());
            if (!selectedFile.exists()) {
                selectedFile.mkdirs();
            }
            loadNovel(frame, selectedFile);


            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    String selPartS = Constants.PROPS.getProperty("selectedPart:" + frame.openFileName);
                    if (!StringUtils.isEmpty(selPartS)) {
                        int selPart = Integer.parseInt(selPartS);
                        frame.dnDTabbedPane.setSelectedIndex(selPart);
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadNovel(final JNovelistFrame frame, final File selectedFolder) {
        try {
            frame.openFileName = selectedFolder.getAbsolutePath();
            frame.dnDTabbedPane = new DnDTabbedPane(DnDTabbedPane.DECIMAL_TO_ROMAN);
            frame.getContentPane().add(frame.dnDTabbedPane, BorderLayout.CENTER);

            File[] ffs = selectedFolder.listFiles();
            if (ffs == null || ffs.length == 0) {
                //new project?
                frame.dnDTabbedPane.newProject();
            } else {
                Arrays.sort(ffs, Utils.FILE_NAME_NUMBER_COMPARATOR);
                for (final File f : ffs) {
                    if (f.isDirectory() && f.list().length > 0 &&
                            StringUtils.isNumeric(f.getName()) &&
                            !f.getName().equals(Constants.HELP_FOLDER_NAME)) {
                        frame.dnDTabbedPane.addNewTab(f);

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < f.list().length; i++) {
                                    String selChapterS = Constants.PROPS.getProperty("selectedChapter:" + i + ":" + frame.openFileName);
                                    if (!StringUtils.isEmpty(selChapterS)) {
                                        int selChapter = Integer.parseInt(selChapterS);

                                        if (frame.dnDTabbedPane.getTabCount()>i && frame.dnDTabbedPane.getComponentAt(i) instanceof DnDTabbedPane) {
                                            DnDTabbedPane pane = (DnDTabbedPane) frame.dnDTabbedPane.getComponentAt(i);
                                            if (pane.getComponentCount() >= selChapter + 1) {
                                                pane.setSelectedIndex(selChapter);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }


            frame.updateStatus();
            frame.updateState();
            frame.validate();
            frame.repaint();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
