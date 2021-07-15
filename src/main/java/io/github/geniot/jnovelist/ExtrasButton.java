package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.model.Chapter;
import io.github.geniot.jnovelist.view.ExtrasDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ExtrasButton extends JToggleButton {
    protected ExtrasDialog extrasDialog;
    private Chapter chapter;
    private List<Chapter> chapterList;

    public ExtrasButton(String label, ExtrasDialog f) {
        super(label);
        this.extrasDialog = f;
        Utils.stylizeButton(this);
    }

    public ExtrasButton(int order, Chapter c, List<Chapter> p, ExtrasDialog f) {
        super(String.valueOf(order));
        this.extrasDialog = f;
        this.chapter = c;
        this.chapterList = p;
        Utils.stylizeButton(this);
        //on select
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                extrasDialog.setChapter(chapter);
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
                if (e.isPopupTrigger() && p.size() > 1) {
                    ExtrasContextPopupMenu popup = new ExtrasContextPopupMenu(ExtrasButton.this, chapter, chapterList, extrasDialog);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private int getChapterButtonIndex() {
        for (int i = 0; i < getParent().getComponentCount(); i++) {
            if (getParent().getComponent(i).equals(this)) {
                return i;
            }
        }
        throw new RuntimeException(this.toString());
    }

}

