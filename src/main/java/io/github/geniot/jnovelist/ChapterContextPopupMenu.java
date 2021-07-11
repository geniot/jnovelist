package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.model.Chapter;
import io.github.geniot.jnovelist.model.Part;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChapterContextPopupMenu extends JPopupMenu {
    public ChapterContextPopupMenu(ChapterButton chapterButton, Chapter chapter, Part part, JNovelistApplication frame) {

        JMenuItem removeMenuItem = new JMenuItem("Delete");
        removeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                part.getChapters().remove(chapter);
                frame.chaptersPanel.remove(chapterButton);
                for (int i = 0; i < frame.chaptersPanel.getComponentCount() - 1; i++) {
                    ChapterButton chapterButton = (ChapterButton) frame.chaptersPanel.getComponent(i);
                    chapterButton.setText(String.valueOf(i + 1));
                }
                frame.selectChapter();
            }
        });
        add(removeMenuItem);
    }

}
