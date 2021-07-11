package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.model.Chapter;
import io.github.geniot.jnovelist.model.Part;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChapterButton extends JToggleButton {
    protected JNovelistApplication frame;
    private Chapter chapter;
    private Part part;

    public ChapterButton(String label, JNovelistApplication f) {
        super(label);
        this.frame = f;
        Utils.stylizeButton(this);
    }

    public ChapterButton(int order, Chapter c, Part p, JNovelistApplication f) {
        super(String.valueOf(order));
        this.frame = f;
        this.chapter = c;
        this.part = p;
        Utils.stylizeButton(this);
        //on select
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.preferences.putInt(JNovelistApplication.Prop.SELECTED_CHAPTER.name(), getChapterButtonIndex() + 1);
                frame.setChapter(chapter);
            }
        });

        //on right mouse click
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger() && part.getChapters().size() > 1) {
                    ChapterContextPopupMenu popup = new ChapterContextPopupMenu(ChapterButton.this, chapter, part, frame);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private int getChapterButtonIndex() {
        for (int i = 0; i < frame.chaptersPanel.getComponentCount(); i++) {
            if (frame.chaptersPanel.getComponent(i).equals(this)) {
                return i;
            }
        }
        throw new RuntimeException(this.toString());
    }

}
