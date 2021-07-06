package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.*;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 19/07/16
 */
public class DictionaryAction extends AbstractNovelistAction implements ActionListener {
    private static String LAST_SEARCH = "";
    private static String LAST_SEARCH_RESULT = "";

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
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());

        searchTextField = new JTextField();
        searchTextField.getCaret().setBlinkRate(0);
        searchTextField.setBorder(BorderFactory.createCompoundBorder(searchTextField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    runSearch();
                }
            }
        });

        JPanel searchFieldWrapper = new JPanel();
        searchFieldWrapper.setLayout(new BorderLayout(0, 0));
        searchFieldWrapper.add(searchTextField, BorderLayout.CENTER);
        int border = 6;
        searchFieldWrapper.setBorder(new EmptyBorder(border,border,border,border));

        contentPane.add(searchFieldWrapper, BorderLayout.NORTH);

        entryArea = new ChapterEditor("", false, Constants.HTML_DOC_START, Constants.HTML_DOC_END);
        entryArea.setBorder(new EmptyBorder(0,border,border,border));
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


        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
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

        if (StringUtils.isNotEmpty(LAST_SEARCH)) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    searchTextField.setText(LAST_SEARCH);
                    entryArea.getDocumentPane().setDocumentText(LAST_SEARCH_RESULT);
                }
            });
        }


        dialog.setVisible(true);
    }

    private void runSearch() {
        LAST_SEARCH_RESULT = Synonymizer.search(searchTextField.getText());
        entryArea.getDocumentPane().setDocumentText(LAST_SEARCH_RESULT);
        if (StringUtils.isNotEmpty(LAST_SEARCH_RESULT)) {
            LAST_SEARCH = searchTextField.getText();
        }
    }
}
