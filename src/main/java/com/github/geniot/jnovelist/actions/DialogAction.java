package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.*;
import com.github.geniot.jnovelist.project.Scene;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


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
        dialog.setModal(actionCommand.equals(Constants.IMAGES_NOVEL_ACTION_COMMAND) ? false : true);
        dialog.setTitle(Constants.VARS.get(actionCommand));

        dnd = new DnDTabbedPane(DnDTabbedPane.INDEX_TO_ALPHABET, actionCommand);
        Scene[] notes = frame.openNovel.getScenes(actionCommand);

        for (Scene note : notes) {
            dnd.addNewTab(note, actionCommand);
        }

        if (dnd.getTabCount() == 1) {
            if (!actionCommand.equals(Constants.IMAGES_NOVEL_ACTION_COMMAND)) {
                dnd.addNewTab(null, actionCommand);
            }
        }

        String sel = Constants.PROPS.getProperty("selectedChapter:" + actionCommand);
        if (sel != null) {
            Integer selIndex = Integer.parseInt(sel);
            if (selIndex + 1 <= dnd.getTabCount()) {
                dnd.setSelectedIndex(selIndex);
            }
        }

        dialog.getContentPane().add(dnd, BorderLayout.CENTER);

        Dimension dim = new Dimension(frame.getWidth(), frame.getHeight());
        dialog.setPreferredSize(dim);
        dialog.setSize(dim);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//        dialog.setMaximumSize(dim);
//        dialog.setMinimumSize(dim);

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
            public void windowOpened(WindowEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < dnd.getTabCount(); i++) {
                            if (dnd.getComponentAt(i) instanceof ChapterEditor) {
                                ChapterEditor chapterEditor = (ChapterEditor) dnd.getComponentAt(i);
                                chapterEditor.getDocumentPane().getEditor().requestFocus();
                            }
                        }
                    }
                });
            }

            @Override
            public void windowClosing(WindowEvent e) {
                save();
                dialog.dispose();
            }

            public void windowClosed(WindowEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (frame.dnDTabbedPane != null && frame.dnDTabbedPane.getSelectedComponent() instanceof DnDTabbedPane) {
                            DnDTabbedPane dnd = (DnDTabbedPane) frame.dnDTabbedPane.getSelectedComponent();
                            if (dnd.getSelectedComponent() instanceof ChapterEditor) {
                                ChapterEditor chapterEditor = (ChapterEditor) dnd.getSelectedComponent();
                                chapterEditor.getDocumentPane().getEditor().requestFocus();
                            }
                        }
                    }
                });
            }
        });


        dialog.setVisible(true);


    }

    private void save() {
        ArrayList<Scene> notes = new ArrayList<Scene>();
        for (int i = 0; i < dnd.getTabCount(); i++) {
            Component c = dnd.getComponentAt(i);
            if (c instanceof ChapterEditor) {
                ChapterEditor editor = (ChapterEditor) c;

                String text = Utils.html2text(editor.getDocumentText());
                if (StringUtils.isBlank(text)) {
                    continue;
                } else {
                    Scene scene = new Scene();
                    scene.setContent(text);
                    scene.setCaretPos(editor.getCaretPosition());
                    JViewport viewport = (JViewport) editor.getDocumentPane().getEditor().getParent();
                    JScrollPane scrollPane = (JScrollPane) viewport.getParent();
                    scene.setViewPos(scrollPane.getVerticalScrollBar().getValue());
                    scene.setSelected(dnd.getSelectedIndex() == i);
                    notes.add(scene);
                }
            }
        }
        frame.openNovel.setScenes(notes.toArray(new Scene[notes.size()]), actionCommand);
    }
}
