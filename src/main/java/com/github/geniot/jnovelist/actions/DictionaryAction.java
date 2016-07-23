package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 19/07/16
 */
public class DictionaryAction extends AbstractNovelistAction implements ActionListener {
    public DictionaryAction(JNovelistFrame f) {
        super(f);
    }

    JDialog dialog;
    ChapterEditor entryArea;
    JTextField searchTextField;
    String actionCommand;

    @Override
    public void actionPerformed(ActionEvent e) {
        this.actionCommand = e.getActionCommand();
        dialog = new JDialog(frame);
        dialog.setModal(true);
        dialog.setTitle(Constants.VARS.get(actionCommand));

        JPanel contentPane = new JPanel(new BorderLayout());

        searchTextField = new JTextField();
        searchTextField.setBorder(BorderFactory.createCompoundBorder(searchTextField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    entryArea.getDocumentPane().setDocumentText(Synonymizer.search(searchTextField.getText()));
                }
            }
        });

        contentPane.add(searchTextField, BorderLayout.NORTH);

        entryArea = new ChapterEditor("");
        contentPane.add(entryArea, BorderLayout.CENTER);

        dialog.getContentPane().add(contentPane, BorderLayout.CENTER);

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
        dialog.pack();
        dialog.setLocationRelativeTo(frame);


        dialog.setVisible(true);
    }
}
