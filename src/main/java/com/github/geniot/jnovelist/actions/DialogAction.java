package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 30/06/15
 */
public class DialogAction extends AbstractNovelistAction implements ActionListener {
    public DialogAction(JNovelistFrame f) {
        super(f);
    }

    JDialog dialog;
    String actionCommand;

    DnDTabbedPane dnd;

    @Override
    public void actionPerformed(ActionEvent e) {
        this.actionCommand = e.getActionCommand();
        dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.setTitle(Constants.VARS.get(actionCommand));
//        List<NovelNote> notes = Constants.getNotesByCommand(actionCommand);

        dnd = new DnDTabbedPane(DnDTabbedPane.INDEX_TO_ALPHABET);
//        if (notes.isEmpty()) {
//            dnd.addNewTab(null);
//        } else {
//            for (NovelNote nn : notes) {
//                dnd.addNewTab(nn);
//            }
//        }

        dialog.getContentPane().add(dnd, BorderLayout.CENTER);
        dialog.setPreferredSize(new Dimension(600, 800));
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        dialog.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
                dialog.dispose();
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                save();
            }
        });
        dialog.setVisible(true);
    }

    private void save() {
//        List<NovelNote> notes = Constants.getNotesByCommand(actionCommand);
//        notes.clear();
//        for (int i = 0; i < dnd.getTabCount(); i++) {
//            Component c = dnd.getComponentAt(i);
//            if (c instanceof ChapterEditor) {
//                ChapterEditor ch = (ChapterEditor) c;
//                NovelNote nn = new NovelNote();
//                nn.setText(ch.getDocumentText());
//                notes.add(nn);
//            }
//        }
    }
}
