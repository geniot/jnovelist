package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.*;
import com.github.geniot.jnovelist.project.Chapter;
import com.github.geniot.jnovelist.project.JNovel;
import com.github.geniot.jnovelist.project.Scene;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


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
//            Constants.PROPS.setProperty(Constants.PROP_LAST_OPEN_DIR, fc.getCurrentDirectory().getPath());
//            if (!selectedFile.exists()) {
//                selectedFile.mkdirs();
//            }
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
        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            Constants.PROPS.setProperty(Constants.PROP_LAST_OPEN_DIR, fc.getCurrentDirectory().toString());
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadNovel(final JNovelistFrame frame, final File selectedFile) {
        try {
            frame.openFileName = selectedFile.getAbsolutePath();
            frame.synopsis = new ChapterEditor(new Scene(), Constants.HTML_SYN_DOC_START, Constants.HTML_SYN_DOC_END);
            frame.dnDTabbedPane = new DnDTabbedPane(DnDTabbedPane.DECIMAL_TO_ROMAN, Constants.LOAD_NOVEL_ACTION_COMMAND);

            frame.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, frame.synopsis, frame.dnDTabbedPane);
            frame.splitPane.setDividerLocation(Constants.PROPS.containsKey(Constants.PROP_DIVIDER_LOCATION) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_DIVIDER_LOCATION)) : 150);
            frame.getContentPane().add(frame.splitPane, BorderLayout.CENTER);


            if (!selectedFile.exists()) {
                //new project?
                frame.dnDTabbedPane.newProject(Constants.LOAD_NOVEL_ACTION_COMMAND);
            } else {

                JAXBContext jaxbContext = JAXBContext.newInstance(JNovel.class);

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

                InputStream stream = null;

                if (selectedFile.getName().endsWith(".zip")) {
                    ZipFile zipFile = new ZipFile(selectedFile);
                    Enumeration<? extends ZipEntry> entries = zipFile.entries();

                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        stream = zipFile.getInputStream(entry);
                    }
                } else {
                    stream = new FileInputStream(selectedFile);
                }

                frame.openNovel = (JNovel) jaxbUnmarshaller.unmarshal(stream);

                stream.close();

                for (Chapter chapter : frame.openNovel.getChapters()) {
                    frame.dnDTabbedPane.addNewTab(chapter, Constants.LOAD_NOVEL_ACTION_COMMAND);
                }

                frame.synopsis.getDocumentPane().setDocumentText(Utils.text2html(frame.openNovel.getSynopsis(), Constants.HTML_SYN_DOC_START, Constants.HTML_SYN_DOC_END));


                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < frame.openNovel.getChapters().length; i++) {
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
        }
    }
}
