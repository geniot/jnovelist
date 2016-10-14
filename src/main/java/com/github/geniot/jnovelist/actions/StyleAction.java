package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.ChapterEditor;
import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;
import com.github.geniot.jnovelist.actions.AbstractNovelistAction;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 20/07/16
 */
public class StyleAction extends AbstractNovelistAction {
    JDialog dialog;
    String actionCommand;
    JTextArea editor;

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
        dialog.getContentPane().add(new JScrollPane(editor), BorderLayout.CENTER);


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
                        String newStyle = editor.getText();
                        if (StringUtils.isNotBlank(newStyle)) {
                            Constants.HTML_DOC_START = newStyle;
                            Constants.PROPS.setProperty(Constants.PROP_STYLE, newStyle);
                        }

                        if (frame.openFileName != null) {
                            Constants.PROPS.setProperty(Constants.PROP_LAST_OPEN_FILE, frame.openFileName);
                            frame.unloadNovel.doClick();

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


