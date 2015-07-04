package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.*;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;


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

        String sel = Constants.PROPS.getProperty("selectedChapter:" + actionCommand);
        if (sel != null) {
            Integer selIndex = Integer.parseInt(sel);
            if (selIndex + 1 <= dnd.getTabCount()) {
                dnd.setSelectedIndex(selIndex);
            }
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
                        if (frame.dnDTabbedPane.getSelectedComponent() instanceof DnDTabbedPane) {
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
        for (int i = 0; i < dnd.getTabCount(); i++) {
            Component c = dnd.getComponentAt(i);
            if (c instanceof ChapterEditor) {
                ChapterEditor editor = (ChapterEditor) c;

                try {
                    String fileName = frame.openFileName + File.separator + Constants.HELP_FOLDER_NAME + File.separator + Constants.VARS.get(actionCommand) + File.separator + (i + 1) + ".txt";
                    File file = new File(fileName);
                    file.getParentFile().mkdirs();

                    Constants.PROPS.put("caretPosition:" + fileName, String.valueOf(editor.getCaretPosition()));
                    Constants.PROPS.put("verticalScrollBar:" + fileName, String.valueOf(editor.getDocumentPane().getVerticalScrollBar().getValue()));

                    String newText = Utils.html2text(editor.getDocumentText());
                    if (file.exists()) {
                        String oldText = FileUtils.readFileToString(file, "UTF-8");
                        if (oldText.equals(newText)) {
                            continue;
                        }
                    }

                    Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                    try {
                        out.write(newText);
                    } finally {
                        out.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        //removing remaining files, obviously removed in UI
        String fileDir = frame.openFileName + File.separator + Constants.HELP_FOLDER_NAME + File.separator + Constants.VARS.get(actionCommand);
        File[] ffs = new File(fileDir).listFiles();
        if (ffs.length > dnd.getTabCount() - 1) {
            Arrays.sort(ffs, Utils.FILE_NAME_NUMBER_COMPARATOR);
            for (int l = dnd.getTabCount() - 1; l < ffs.length; l++) {
                String fileName = fileDir + File.separator + (l + 1) + ".txt";
                File f = new File(fileName);
                if (f.exists()) {
                    f.delete();
                }
            }
        }

        Constants.PROPS.put("selectedChapter:" + actionCommand, String.valueOf(dnd.getSelectedIndex()));

    }
}
