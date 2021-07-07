package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.JNovelistFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 20/07/16
 */
public class StyleAction extends AbstractNovelistAction {
    JDialog dialog;
    String actionCommand;
    JTextArea editor;
    JTextArea synopsisEditor;

    public StyleAction(JNovelistFrame f) {
        super(f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.actionCommand = e.getActionCommand();
        dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.setTitle(Constants.VARS.get(actionCommand));
        dialog.getContentPane().setLayout(new BorderLayout());

        editor = new JTextArea(Constants.HTML_DOC_START);
        editor.setLineWrap(true);
        editor.setWrapStyleWord(true);
        editor.setFont(editor.getFont().deriveFont(20f));

        synopsisEditor = new JTextArea(Constants.HTML_SYN_DOC_START);
        synopsisEditor.setLineWrap(true);
        synopsisEditor.setWrapStyleWord(true);
        synopsisEditor.setFont(synopsisEditor.getFont().deriveFont(20f));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(editor), new JScrollPane(synopsisEditor));
        splitPane.setDividerLocation(this.frame.getHeight()/2);
        dialog.getContentPane().add(splitPane, BorderLayout.CENTER);


        Dimension dim = new Dimension(frame.getWidth(), frame.getHeight());
        dialog.setPreferredSize(dim);
        dialog.setSize(dim);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

        dialog.getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Constants.HTML_DOC_START = editor.getText();
                        Constants.PROPS.setProperty(Constants.PROP_STYLE, editor.getText());

                        Constants.HTML_SYN_DOC_START = synopsisEditor.getText();
                        Constants.PROPS.setProperty(Constants.PROP_SYNOPSIS_STYLE, synopsisEditor.getText());

                        if (frame.openFileName != null) {
                            frame.loadNovel.doClick();

                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    frame.load();
                                }
                            });

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


