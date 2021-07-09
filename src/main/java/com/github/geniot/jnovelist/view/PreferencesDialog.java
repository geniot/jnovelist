package com.github.geniot.jnovelist.view;

import com.github.geniot.jnovelist.ChapterEditor;
import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.JNovelistFrame;
import com.github.geniot.jnovelist.Utils;
import com.github.geniot.jnovelist.model.Chapter;
import com.github.geniot.jnovelist.model.LanguageElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.SortedSet;

public class PreferencesDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox themeComboBox;
    private JComboBox languageComboBox;
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

        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        SortedSet<LanguageElement> langs = Utils.getLanguages();
        for (LanguageElement lang : langs) {
            comboBoxModel.addElement(lang);
        }
        languageComboBox.setModel(comboBoxModel);

        String code = Constants.PROPS.getProperty(Constants.PropKey.PROP_LANGUAGE.name());
        languageComboBox.setSelectedItem(Utils.findElementByCode(languageComboBox.getModel(), code));

        getRootPane().registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        backgroundColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(PreferencesDialog.this, "Choose a color", Utils.hex2Rgb(backgroundColorButton.getText()));
                if (newColor != null) {
                    String newColorStr = Utils.color2hex(newColor);
                    backgroundColorButton.setText(newColorStr);
                    updatePreview();
                }
            }
        });

        backgroundColorButton.setText(Constants.PROPS.getProperty(Constants.PropKey.PROP_BG_COLOR.name()));

        themeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = chapterEditor.getDocumentText();
//                if (frame != null) {
//                    Utils.setLAF(newLAF, frame);
//                }
                Utils.setLAF(themeComboBox.getSelectedItem().toString(), PreferencesDialog.this);
                chapterEditor.getDocumentPane().setDocumentText(text);
            }
        });


        textColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(PreferencesDialog.this, "Choose a color", Utils.hex2Rgb(textColorButton.getText()));
                if (newColor != null) {
                    String newColorStr = Utils.color2hex(newColor);
                    textColorButton.setText(newColorStr);
                    chapterEditor.getEditorPane().setCaretColor(newColor);
                    updatePreview();
                }
            }
        });

        String colorStr = Constants.PROPS.getProperty(Constants.PropKey.PROP_TXT_COLOR.name());
        textColorButton.setText(colorStr);
        chapterEditor.getEditorPane().setCaretColor(Utils.hex2Rgb(colorStr));


        fontComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        });
        fontComboBox.setSelectedItem(Constants.PROPS.getProperty(Constants.PropKey.PROP_FONT_FACE.name()));

        fontSizeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        });
        fontSizeComboBox.setSelectedItem(Constants.PROPS.getProperty(Constants.PropKey.PROP_FONT_SIZE.name()));

        borderMarginComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        });
        borderMarginComboBox.setSelectedItem(Constants.PROPS.getProperty(Constants.PropKey.PROP_MARGIN.name()));

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

        themeComboBox.setSelectedItem(Constants.PROPS.getProperty(Constants.PropKey.PROP_LAF.name()));

        chapterEditor.getEditorPane().getCaret().setBlinkRate(0);
        pack();
        if (frame != null) {
            setLocationRelativeTo(frame);
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                chapterEditor.getSHTMLEditorPane().setCaretPosition(chapterEditor.getDocument().getLength() - 1);
                chapterEditor.getEditorPane().requestFocus();
            }
        });
    }

    private void updatePreview() {
        Object[] args = new Object[]{
                backgroundColorButton.getText(),
                textColorButton.getText(),
                fontComboBox.getSelectedItem().toString(),
                Integer.parseInt(fontSizeComboBox.getSelectedItem().toString()),
                Integer.parseInt(borderMarginComboBox.getSelectedItem().toString())
        };
        chapterEditor.getDocumentPane().setDocumentText(Utils.text2html(Utils.html2text(chapterEditor.getDocumentText()), Constants.HTML_DOC_START(args), Constants.HTML_DOC_END));
    }

    private void onOK() {
        String oldLaf = Constants.PROPS.getProperty(Constants.PropKey.PROP_LAF.name());
        String newLaf = themeComboBox.getSelectedItem().toString();
        if (!oldLaf.equals(newLaf)) {
            Constants.PROPS.setProperty(Constants.PropKey.PROP_LAF.name(), newLaf);
            Utils.setLAF(newLaf, frame);
        }
        Constants.PROPS.setProperty(Constants.PropKey.PROP_LANGUAGE.name(), ((LanguageElement) languageComboBox.getSelectedItem()).getCode());
        Constants.PROPS.setProperty(Constants.PropKey.PROP_BG_COLOR.name(), backgroundColorButton.getText());
        Constants.PROPS.setProperty(Constants.PropKey.PROP_TXT_COLOR.name(), textColorButton.getText());
        Constants.PROPS.setProperty(Constants.PropKey.PROP_FONT_FACE.name(), fontComboBox.getSelectedItem().toString());
        Constants.PROPS.setProperty(Constants.PropKey.PROP_FONT_SIZE.name(), fontSizeComboBox.getSelectedItem().toString());
        Constants.PROPS.setProperty(Constants.PropKey.PROP_MARGIN.name(), borderMarginComboBox.getSelectedItem().toString());
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
