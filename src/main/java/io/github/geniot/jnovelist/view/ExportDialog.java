package io.github.geniot.jnovelist.view;

import io.github.geniot.jnovelist.ChapterEditor;
import io.github.geniot.jnovelist.model.Chapter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.util.List;

public class ExportDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel viewerPanel;
    private JButton copyToClipboardButton;

    public ExportDialog(JNovelistApplication frame) {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Export");
        getRootPane().setDefaultButton(buttonOK);

        List<String> novelTextList = frame.novel.exportNovel();
        String[] novelTextArray = novelTextList.toArray(new String[novelTextList.size()]);

        Chapter chapter = new Chapter();
        chapter.setLines(novelTextArray);
        ChapterEditor chapterEditor = new ChapterEditor(chapter, frame);
        chapterEditor.getEditorPane().setEditable(false);
        viewerPanel.add(chapterEditor, BorderLayout.CENTER);


        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        copyToClipboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(new StringSelection(String.join("\n", novelTextArray)), null);
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
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ExportDialog dialog = new ExportDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
