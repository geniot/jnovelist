package com.github.geniot.jnovelist.actions;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.geniot.jnovelist.*;
import com.github.geniot.jnovelist.model.Chapter;
import com.github.geniot.jnovelist.model.JNovel;
import com.github.geniot.jnovelist.model.Part;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

import static com.github.geniot.jnovelist.Utils.getGitRootDir;
import static com.github.geniot.jnovelist.Utils.runCommand;


/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class SaveAction extends AbstractNovelistAction {
    private static final Logger logger = Logger.getLogger(SaveAction.class.getName());

    private FileComparator fileComparator = new FileComparator();

    public SaveAction(JNovelistFrame f) {
        super(f);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        save();
        try {
            File f = getGitRootDir(frame.openFileName);
            if (f != null) {
                System.out.println(runCommand("git add -A", f));
                System.out.println(runCommand("git commit -m \"" + System.currentTimeMillis() + "\"", f));
                //quick squash
//                System.out.println(runCommand("git checkout --orphan new-master master", f));
//                System.out.println(runCommand("git commit -m \"" + System.currentTimeMillis() + "\"", f));
//                System.out.println(runCommand("git branch -M new-master master", f));

                System.out.println(runCommand("git rebase --root", f));
                System.out.println(runCommand("git push", f));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        frame.saveNovel.setEnabled(false);
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
//        ArrayList<Part> parts = new ArrayList<Part>();
//        for (int i = 0; i < frame.dnDTabbedPane.getTabCount(); i++) {
//            Component c = frame.dnDTabbedPane.getComponentAt(i);
//            if (c instanceof DnDTabbedPane) {
//                DnDTabbedPane dnd = (DnDTabbedPane) c;
//                ButtonTabComponent tabComponent = (ButtonTabComponent) frame.dnDTabbedPane.getTabComponentAt(i);
//                Part part = new Part();
////                chapter.setDescription(tabComponent.getText());
////                part.setSelected(frame.dnDTabbedPane.getSelectedComponent().equals(dnd));
//
//                ArrayList<Chapter> chapters = new ArrayList<Chapter>();
//                for (int k = 0; k < dnd.getTabCount(); k++) {
//                    Component o = dnd.getComponentAt(k);
//
//                    if (o instanceof ChapterEditor) {
//                        ChapterEditor editor = (ChapterEditor) o;
//                        ButtonTabComponent chapterTabComponent = (ButtonTabComponent) dnd.getTabComponentAt(k);
//
//                        Chapter chapter = new Chapter();
////                        scene.setDescription(chapterTabComponent.getText());
////                        chapter.setCaretPos(editor.getCaretPosition());
//                        JViewport viewport = (JViewport) editor.getDocumentPane().getEditor().getParent();
//                        JScrollPane scrollPane = (JScrollPane) viewport.getParent();
////                        chapter.setViewPos(scrollPane.getVerticalScrollBar().getValue());
//
//                        String text = Utils.html2text(editor.getDocumentText());
//                        chapter.setLines(text.split("\n"));
//                        chapters.add(chapter);
//                    }
//                }
//                part.setChapters(chapters);
//                parts.add(part);
//            }
//
//        }

        if (frame.openNovel == null) {
            frame.openNovel = new JNovel();
        }
//        frame.openNovel.setParts(parts);

        try {
            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
            prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
            String projectJSON = new ObjectMapper().writer(prettyPrinter).writeValueAsString(frame.openNovel);
            FileUtils.writeStringToFile(new File(frame.openFileName), projectJSON);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class FileComparator implements Comparator<File> {


        @Override
        public int compare(File f1, File f2) {
            String[] parts1 = f1.getAbsolutePath().split("\\.");
            String[] parts2 = f2.getAbsolutePath().split("\\.");
            Long ts1 = Long.parseLong(parts1[parts1.length - 2]);
            Long ts2 = Long.parseLong(parts2[parts2.length - 2]);
            return ts1.compareTo(ts2);
        }
    }
}
