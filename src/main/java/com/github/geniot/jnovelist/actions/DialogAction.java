package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


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

        dnd = new DnDTabbedPane(DnDTabbedPane.INDEX_TO_ALPHABET);
        File f = new File(frame.openFileName + File.separator + Constants.HELP_FOLDER_NAME + File.separator + Constants.VARS.get(actionCommand));
        if (f.exists() && f.isDirectory()) {
            File[] ffs = f.listFiles();
            for (File note : ffs) {
                dnd.addNewTab(note);
            }
        } else {
            dnd.addNewTab(null);
        }

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
                dialog.dispose();
            }
        });
        dialog.setVisible(true);
    }

    private void save() {
        for (int i = 0; i < dnd.getTabCount(); i++) {
            Component c = dnd.getComponentAt(i);
            if (c instanceof ChapterEditor) {
                ChapterEditor editor = (ChapterEditor) c;
                try {
                    File file = new File(frame.openFileName + File.separator + Constants.HELP_FOLDER_NAME + File.separator + Constants.VARS.get(actionCommand)+File.separator + (i + 1) + ".txt");
                    file.getParentFile().mkdirs();
                    Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                    try {
                        out.write(Utils.html2text(editor.getDocumentText()));
                    } finally {
                        out.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
