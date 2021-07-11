package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.model.Chapter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChapterButton extends JToggleButton {
    protected JNovelistApplication frame;
    private Chapter chapter;

    public ChapterButton(String label, JNovelistApplication f) {
        super(label);
        this.frame = f;
        Utils.stylizeButton(this);
    }

    public ChapterButton(int order, Chapter c, JNovelistApplication f) {
        super(String.valueOf(order));
        this.frame = f;
        this.chapter = c;
        Utils.stylizeButton(this);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.preferences.putInt(JNovelistApplication.Prop.SELECTED_CHAPTER.name(), order);
                frame.setChapter(chapter);
            }
        });
    }

}
