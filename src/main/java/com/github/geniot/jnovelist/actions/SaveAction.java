package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.*;
import com.github.geniot.jnovelist.project.Chapter;
import com.github.geniot.jnovelist.project.JNovel;
import com.github.geniot.jnovelist.project.Scene;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class SaveAction extends AbstractNovelistAction {
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
        Constants.PROPS.setProperty(Constants.PROP_DIVIDER_LOCATION, String.valueOf((int) frame.splitPane.getDividerLocation()));
        ArrayList<Chapter> chapters = new ArrayList<Chapter>();
        for (int i = 0; i < frame.dnDTabbedPane.getTabCount(); i++) {
            Component c = frame.dnDTabbedPane.getComponentAt(i);
            if (c instanceof DnDTabbedPane) {
                DnDTabbedPane dnd = (DnDTabbedPane) c;
                ButtonTabComponent tabComponent = (ButtonTabComponent) frame.dnDTabbedPane.getTabComponentAt(i);
                Chapter chapter = new Chapter();
//                chapter.setDescription(tabComponent.getText());
                chapter.setSelected(frame.dnDTabbedPane.getSelectedComponent().equals(dnd));

                ArrayList<Scene> scenes = new ArrayList<Scene>();
                for (int k = 0; k < dnd.getTabCount(); k++) {
                    Component o = dnd.getComponentAt(k);

                    if (o instanceof ChapterEditor) {
                        ChapterEditor editor = (ChapterEditor) o;
                        ButtonTabComponent chapterTabComponent = (ButtonTabComponent) dnd.getTabComponentAt(k);

                        Scene scene = new Scene();
//                        scene.setDescription(chapterTabComponent.getText());
                        scene.setCaretPos(editor.getCaretPosition());
                        scene.setViewPos(editor.getDocumentPane().getVerticalScrollBar().getValue());

                        String text = Utils.html2text(editor.getDocumentText());
                        scene.setContent(text);
                        scenes.add(scene);
                    }
                }
                chapter.setScenes(scenes.toArray(new Scene[scenes.size()]));
                chapters.add(chapter);
            }

        }

        if (frame.openNovel == null) {
            frame.openNovel = new JNovel();
        }
        frame.openNovel.setChapters(chapters.toArray(new Chapter[chapters.size()]));
        frame.openNovel.setSynopsis(Utils.html2text(frame.synopsis.getDocumentText()));

        try {
            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            JAXBContext jaxbContext = JAXBContext.newInstance(JNovel.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(frame.openNovel, fos);

            String fileName = getFileName(frame.openFileName);
            String newFileName = fileName + "." + System.currentTimeMillis() + ".zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(newFileName));
            Constants.PROPS.setProperty(Constants.PROP_LAST_OPEN_FILE, newFileName);

            ZipEntry e = new ZipEntry(new File(fileName).getName());
            out.putNextEntry(e);
            byte[] data = fos.toByteArray();
            out.write(data, 0, data.length);
            out.closeEntry();
            out.close();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getFileName(String openFileName) {
        if (openFileName.endsWith(".zip")) {
            return openFileName.split("\\.xml\\.")[0] + ".xml";
        } else {
            return openFileName;
        }
    }
}
