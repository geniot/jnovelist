package io.github.geniot.jnovelist.view;

import io.github.geniot.jnovelist.ChapterEditor;
import io.github.geniot.jnovelist.Synonymizer;
import io.github.geniot.jnovelist.model.Chapter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class DictionaryDialog extends JDialog {
    private static String LAST_SEARCH = "";
    private static String LAST_SEARCH_RESULT = "";
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField searchTextField;
    private JPanel viewerPanel;
    private ChapterEditor chapterEditor;

    public DictionaryDialog(JNovelistApplication frame) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Synonyms");
        getRootPane().setDefaultButton(buttonOK);

        searchTextField.setBorder(new EmptyBorder(4, 4, 4, 4));
        searchTextField.getCaret().setBlinkRate(0);

        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    runSearch();
                }
            }
        });


        Chapter chapter = new Chapter();
        chapterEditor = new ChapterEditor(chapter, frame);
        chapterEditor.setBorder(new LineBorder(Color.BLACK));
        chapterEditor.getEditorPane().setEditable(false);
        viewerPanel.add(chapterEditor, BorderLayout.CENTER);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setLocationRelativeTo(frame);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                searchTextField.requestFocus();
            }
        });
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void runSearch() {
        LAST_SEARCH_RESULT = Synonymizer.search(searchTextField.getText());
        chapterEditor.getDocumentPane().setDocumentText(LAST_SEARCH_RESULT);
        if (StringUtils.isNotEmpty(LAST_SEARCH_RESULT)) {
            LAST_SEARCH = searchTextField.getText();
        }
    }

}
