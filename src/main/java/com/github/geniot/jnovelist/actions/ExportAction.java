package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.ChapterEditor;
import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 06/07/16
 */
public class ExportAction extends AbstractNovelistAction {
    JDialog dialog;
    String actionCommand;

    public ExportAction(JNovelistFrame f) {
        super(f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.actionCommand = e.getActionCommand();
        dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.setTitle(frame.getNovelName());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        final String novelText = frame.getNovelText();
        dialog.getContentPane().add(new ChapterEditor(novelText,false), BorderLayout.CENTER);
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        JButton copy2clipboard = new JButton("Copy to clipboard");
        copy2clipboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(new StringSelection(novelText), null);
            }
        });
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(copy2clipboard);

        dialog.getContentPane().add(toolbar, BorderLayout.SOUTH);

        Dimension dim = new Dimension(frame.getWidth(), frame.getHeight());
        dialog.setPreferredSize(dim);
        dialog.setSize(dim);
//        dialog.setMaximumSize(dim);
//        dialog.setMinimumSize(dim);

        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

        dialog.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        dialog.addWindowListener(new WindowAdapter() {

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

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }


}
