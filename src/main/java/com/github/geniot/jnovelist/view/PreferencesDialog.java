package com.github.geniot.jnovelist.view;

import com.github.geniot.jnovelist.ChapterEditor;
import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.JNovelistFrame;
import com.github.geniot.jnovelist.Utils;
import com.github.geniot.jnovelist.model.Chapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PreferencesDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox themeComboBox;
    private JComboBox comboBox2;
    private JButton backgroundColorButton;
    private JButton textColorButton;
    private JComboBox fontComboBox;
    private JComboBox fontSizeComboBox;
    private JComboBox borderMarginComboBox;
    private JPanel previewPanel;
    private JButton backgroundChooseButton;
    private JNovelistFrame frame;
    private ChapterEditor chapterEditor;

    public PreferencesDialog(JNovelistFrame f) {
        this.frame = f;
        setContentPane(contentPane);
        setModal(true);
        setIconImage(Constants.ICON);
        getRootPane().setDefaultButton(buttonOK);
        setTitle(Constants.RES.getString("title.preferences"));
        setPreferredSize(new Dimension(600, 800));
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontComboBox.setModel(new DefaultComboBoxModel(fonts));

        Chapter chapter = new Chapter();
        chapter.setLines(new String[]{Constants.LOREM_IPSUM});
        chapterEditor = new ChapterEditor(chapter, Constants.HTML_DOC_START(), Constants.HTML_DOC_END);
//        chapterEditor.getEditorPane().setEditable(false);
        previewPanel.add(chapterEditor, BorderLayout.CENTER);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);

        getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        backgroundColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(frame, "Choose a color", Utils.hex2Rgb(backgroundColorButton.getText()));
                if (newColor != null) {
                    String newColorStr = Utils.color2hex(newColor);
                    Constants.PROPS.setProperty(Constants.PropKey.PROP_BG_COLOR.name(), newColorStr);
                    backgroundColorButton.setText(newColorStr);
                    updatePreview();
                }
            }
        });

        themeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newLAF = Constants.LAF_PREFIX + themeComboBox.getSelectedItem().toString().toLowerCase() + "." + themeComboBox.getSelectedItem() + Constants.LAF_SUFFIX;
                String text = chapterEditor.getDocumentText();
//                if (frame != null) {
//                    Utils.setLAF(newLAF, frame);
//                }
                Utils.setLAF(newLAF, PreferencesDialog.this);
                chapterEditor.getDocumentPane().setDocumentText(text);
            }
        });

        textColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(frame, "Choose a color", Utils.hex2Rgb(textColorButton.getText()));
                if (newColor != null) {
                    String newColorStr = Utils.color2hex(newColor);
                    Constants.PROPS.setProperty(Constants.PropKey.PROP_TXT_COLOR.name(), newColorStr);
                    textColorButton.setText(newColorStr);
                    chapterEditor.getEditorPane().setCaretColor(newColor);
                    updatePreview();
                }
            }
        });

        fontComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Constants.PROPS.setProperty(Constants.PropKey.PROP_FONT_FACE.name(), fontComboBox.getSelectedItem().toString());
                updatePreview();
            }
        });

        fontSizeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Constants.PROPS.setProperty(Constants.PropKey.PROP_FONT_SIZE.name(), fontSizeComboBox.getSelectedItem().toString());
                updatePreview();
            }
        });

        borderMarginComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Constants.PROPS.setProperty(Constants.PropKey.PROP_MARGIN.name(), borderMarginComboBox.getSelectedItem().toString());
                updatePreview();
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

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
        if (frame != null) {
            setLocationRelativeTo(frame);
        }
    }

    private void updatePreview() {
        chapterEditor.getDocumentPane().setDocumentText(Utils.text2html(Utils.html2text(chapterEditor.getDocumentText()), Constants.HTML_DOC_START(), Constants.HTML_DOC_END));
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
        PreferencesDialog dialog = new PreferencesDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
