package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;
import com.github.geniot.jnovelist.Utils;
import com.github.geniot.jnovelist.model.Chapter;
import com.github.geniot.jnovelist.model.ITextable;
import com.github.geniot.jnovelist.model.PersistedModel2;

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

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (frame.openFileName != null) {
                frame.unloadNovel.doClick();
            }

            File selectedFile = fc.getSelectedFile();
            Constants.PROPS.setProperty(Constants.PROP_LAST_OPEN_DIR, fc.getCurrentDirectory().getPath());
            loadNovel(frame, selectedFile);
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadNovel(JNovelistFrame frame, File selectedFile) {
        try {
            PersistedModel2 model = Utils.loadModel(selectedFile);


            frame.openFileName = selectedFile.getAbsolutePath();
            frame.dnDTabbedPane = new DnDTabbedPane(DnDTabbedPane.DECIMAL_TO_ROMAN);
            frame.getContentPane().add(frame.dnDTabbedPane, BorderLayout.CENTER);


            int chaptersCount = model.getNovel().size();
            int currentPart = -1;
            int selectedIndex = 0;

            for (int i = 0; i < chaptersCount; i++) {
                final Chapter chapter = model.getNovel().get(i);
                if (chapter == null) {
                    continue;
                }

                ITextable it = new ITextable() {
                    @Override
                    public String getText() {
                        return chapter.getText();
                    }

                    @Override
                    public int getViewPosition() {
                        return chapter.getViewPosition();
                    }

                    @Override
                    public int getCaretPosition() {
                        return chapter.getCaretPosition();
                    }
                };

                if (chapter.getPart() != currentPart) {
                    frame.dnDTabbedPane.addNewTab(it);
                    currentPart = chapter.getPart();
                    selectedIndex = 0;
                }
                DnDTabbedPane partTab = (DnDTabbedPane) frame.dnDTabbedPane.getComponentAt(currentPart);
                partTab.addNewTab(it);
                if (chapter.isSelected()) {
                    selectedIndex = partTab.getTabCount() - 2;
                }
                partTab.setSelectedIndex(selectedIndex);
            }

            if (chaptersCount == 0) {
                frame.dnDTabbedPane.addNewTab(null);
            }

            Constants.NOTES_HEROES = model.getHeroes();
            Constants.NOTES_LOCATIONS = model.getLocations();
            Constants.NOTES_THINGS = model.getThings();
            Constants.NOTES_NOTES = model.getNotes();


            Object o = model.getProperties().get(Constants.PROP_SELECTED_PART);
            frame.dnDTabbedPane.setSelectedIndex(o == null ? 0 : Integer.parseInt(o.toString()));


            frame.updateStatus();
            frame.updateState();
            frame.validate();
            frame.repaint();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
