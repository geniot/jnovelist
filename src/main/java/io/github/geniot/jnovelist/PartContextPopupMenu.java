package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.model.JNovel;
import io.github.geniot.jnovelist.model.Part;
import io.github.geniot.jnovelist.view.JNovelistApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PartContextPopupMenu extends JPopupMenu {
    public PartContextPopupMenu(PartButton partButton, Part part, JNovel novel, JNovelistApplication frame) {

        JMenuItem removeMenuItem = new JMenuItem("Delete");
        removeMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novel.getParts().remove(part);
                frame.partsPanel.remove(partButton);
                for (int i = 0; i < frame.partsPanel.getComponentCount() - 1; i++) {
                    PartButton partButton = (PartButton) frame.partsPanel.getComponent(i);
                    partButton.setText(Utils.decimalIndexToRoman(i + 1));
                }
                frame.selectPart();
            }
        });
        add(removeMenuItem);
    }

}
