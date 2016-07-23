package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.ChapterEditor;
import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;
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
public class InfoAction extends AbstractNovelistAction {
    JDialog dialog;
    String actionCommand;

    public InfoAction(JNovelistFrame f) {
        super(f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.actionCommand = e.getActionCommand();
        dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.setTitle(Constants.VARS.get(actionCommand));
        dialog.getContentPane().setLayout(new BorderLayout());

        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(InfoAction.class.getClassLoader().getResourceAsStream("info.txt"), "UTF8"));
            String str;
            while ((str = in.readLine()) != null) {
                if (StringUtils.isEmpty(str)){
                    continue;
                }
                sb.append("<p>");
                sb.append(str);
                sb.append("</p>");
            }

            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        dialog.getContentPane().add(new ChapterEditor(sb.toString()), BorderLayout.CENTER);


        Dimension dim = new Dimension(frame.getWidth(), frame.getHeight());
        dialog.setPreferredSize(dim);
        dialog.setSize(dim);

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
                        if (frame.dnDTabbedPane!=null && frame.dnDTabbedPane.getSelectedComponent() instanceof DnDTabbedPane) {
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

