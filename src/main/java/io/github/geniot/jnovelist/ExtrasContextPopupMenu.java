package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.model.Chapter;
import io.github.geniot.jnovelist.model.Part;
import io.github.geniot.jnovelist.view.ExtrasDialog;
import io.github.geniot.jnovelist.view.JNovelistApplication;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ExtrasContextPopupMenu extends JPopupMenu {
    public ExtrasContextPopupMenu(ExtrasButton chapterButton, Chapter chapter, List<Chapter> chapterList, ExtrasDialog extrasDialog) {

        JMenuItem removeMenuItem = new JMenuItem("Delete");
        removeMenuItem.addActionListener(e -> {
            chapterList.remove(chapter);
            extrasDialog.chaptersPanel.remove(chapterButton);
            for (int i = 0; i < extrasDialog.chaptersPanel.getComponentCount() - 1; i++) {
                ExtrasButton chapterButton1 = (ExtrasButton) extrasDialog.chaptersPanel.getComponent(i);
                chapterButton1.setText(String.valueOf(i + 1));
            }
            extrasDialog.selectChapter();
        });
        add(removeMenuItem);
    }

}
