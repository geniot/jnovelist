package com.github.geniot.jnovelist.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;
import com.github.geniot.jnovelist.model.JNovel;
import com.github.geniot.jnovelist.model.Part;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
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

    public boolean isLoading = false;

    public LoadNovelAction(JNovelistFrame f) {
        super(f);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc;
        if (Constants.PROPS.containsKey(Constants.PropKey.PROP_LAST_OPEN_DIR.name())) {
            try {
                fc = new JFileChooser(Constants.PROPS.getProperty(Constants.PropKey.PROP_LAST_OPEN_DIR.name()));
            } catch (Exception ex) {
                logger.log(Level.WARNING, ex.getMessage());
                fc = new JFileChooser();
            }
        } else {
            fc = new JFileChooser();
        }

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".json") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "JNovelist project file in JSON format";
            }
        });

        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (frame.openFileName != null) {
                frame.unloadNovel.doClick();
            }
            Constants.PROPS.setProperty(Constants.PropKey.PROP_LAST_OPEN_DIR.name(), fc.getCurrentDirectory().toString());

            File selectedFile = fc.getSelectedFile();
//            Constants.PROPS.setProperty(Constants.PROP_LAST_OPEN_DIR, fc.getCurrentDirectory().getPath());
//            if (!selectedFile.exists()) {
//                selectedFile.mkdirs();
//            }
            loadNovel(frame, selectedFile);

        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            Constants.PROPS.setProperty(Constants.PropKey.PROP_LAST_OPEN_DIR.name(), fc.getCurrentDirectory().toString());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadNovel(final JNovelistFrame frame, final File selectedFile) {
        try {
            isLoading = true;
            String suffix = selectedFile.getAbsolutePath().endsWith(".json") ? "" : ".json";
            frame.openFileName = selectedFile.getAbsolutePath() + suffix;
            frame.dnDTabbedPane = new DnDTabbedPane(DnDTabbedPane.DECIMAL_TO_ROMAN, Constants.LOAD_NOVEL_ACTION_COMMAND);

            frame.getContentPane().add(frame.dnDTabbedPane, BorderLayout.CENTER);


            if (!selectedFile.exists()) {
                //new project?
                frame.dnDTabbedPane.newProject(Constants.LOAD_NOVEL_ACTION_COMMAND);
            } else {
                String projectJson = FileUtils.readFileToString(selectedFile);
                frame.openNovel = (JNovel) new ObjectMapper().readValue(projectJson, JNovel.class);

                for (Part part : frame.openNovel.getParts()) {
                    frame.dnDTabbedPane.addNewTab(part, Constants.LOAD_NOVEL_ACTION_COMMAND);
                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        String selPartS = Constants.PROPS.getProperty("selectedPart:" + frame.openFileName);
                        if (!StringUtils.isEmpty(selPartS)) {
                            int selPart = Integer.parseInt(selPartS);
                            frame.dnDTabbedPane.setSelectedIndex(selPart);
                        }

                        for (int i = 0; i < frame.openNovel.getParts().size(); i++) {
                            String selChapterS = Constants.PROPS.getProperty("selectedChapter:" + i + ":" + frame.openFileName);
                            if (!StringUtils.isEmpty(selChapterS)) {
                                int selChapter = Integer.parseInt(selChapterS);

                                if (frame.dnDTabbedPane.getTabCount() > i && frame.dnDTabbedPane.getComponentAt(i) instanceof DnDTabbedPane) {
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


            frame.updateStatus();
            frame.updateState();
            frame.validate();
            frame.repaint();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            isLoading = false;
        }
    }
}
