package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.model.JNovel;
import io.github.geniot.jnovelist.model.Part;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PartButton extends ChapterButton {
    Part part;
    JNovel novel;

    public PartButton(int order, Part p, JNovel n, JNovelistApplication f) {
        super(Utils.decimalIndexToRoman(order), f);
        this.part = p;
        this.novel = n;

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.preferences.putInt(JNovelistApplication.Prop.SELECTED_PART.name(), getPartButtonIndex() + 1);
                frame.setPart(part);
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
                if (e.isPopupTrigger() && novel.getParts().size() > 1) {
                    PartContextPopupMenu popup = new PartContextPopupMenu(PartButton.this, part, novel, frame);
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private int getPartButtonIndex() {
        for (int i = 0; i < frame.partsPanel.getComponentCount(); i++) {
            if (frame.partsPanel.getComponent(i).equals(this)) {
                return i;
            }
        }
        throw new RuntimeException(this.toString());
    }
}
