package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.model.Part;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PartButton extends ChapterButton {
    Part part;

    public PartButton(int order, Part p, JNovelistApplication f) {
        super(Utils.decimalIndexToRoman(order), f);
        this.part = p;
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.preferences.putInt(JNovelistApplication.Prop.SELECTED_PART.name(), order);
                frame.setPart(part);
            }
        });
    }
}
