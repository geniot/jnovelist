package io.github.geniot.jnovelist.view;

import io.github.geniot.jnovelist.ChapterEditor;
import io.github.geniot.jnovelist.DesktopApplication;
import io.github.geniot.jnovelist.Utils;
import io.github.geniot.jnovelist.model.Chapter;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PreferencesDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox themeComboBox;
    private JButton backgroundColorButton;
    private JButton textColorButton;
    private JComboBox fontComboBox;
    private JComboBox fontSizeComboBox;
    private JComboBox borderMarginComboBox;
    private JPanel previewPanel;
    private JNovelistApplication frame;
    private ChapterEditor chapterEditor;

    private static String LOREM_IPSUM;

    static {
        try {
            LOREM_IPSUM = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("lorem_ipsum.txt"), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PreferencesDialog(JNovelistApplication f) {
        this.frame = f;
        setContentPane(contentPane);
        setModal(true);
        setIconImage(DesktopApplication.ICON);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Preferences");
        setPreferredSize(new Dimension(600, 800));
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontComboBox.setModel(new DefaultComboBoxModel(fonts));

        Chapter chapter = new Chapter();
        chapter.setLines(new String[]{LOREM_IPSUM});
        chapterEditor = new ChapterEditor(chapter, frame);
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
                Color newColor = JColorChooser.showDialog(PreferencesDialog.this, "Choose a color", Utils.hex2Rgb(backgroundColorButton.getText()));
                if (newColor != null) {
                    String newColorStr = Utils.color2hex(newColor);
                    backgroundColorButton.setText(newColorStr);
                    updatePreview();
                }
            }
        });

        backgroundColorButton.setText(frame.preferences.get(ChapterEditor.Prop.PROP_BG_COLOR.name(), "#FFFFFF"));

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

        String txtColor = frame.preferences.get(ChapterEditor.Prop.PROP_TXT_COLOR.name(), "#000000");
        textColorButton.setText(txtColor);
        chapterEditor.getEditorPane().setCaretColor(Utils.hex2Rgb(txtColor));


        fontComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        });
        fontComboBox.setSelectedItem(frame.preferences.get(ChapterEditor.Prop.PROP_FONT_FACE.name(), "Arial"));

        fontSizeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        });
        fontSizeComboBox.setSelectedItem(String.valueOf(frame.preferences.getInt(ChapterEditor.Prop.PROP_FONT_SIZE.name(), 12)));

        borderMarginComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        });
        borderMarginComboBox.setSelectedItem(String.valueOf(frame.preferences.getInt(ChapterEditor.Prop.PROP_MARGIN.name(), 10)));

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

        themeComboBox.setSelectedItem(frame.preferences.get(ChapterEditor.Prop.PROP_LAF.name(), "Luna"));


        pack();
        if (frame != null) {
            setLocationRelativeTo(frame);
        }
    }

    private void updatePreview() {
        String bgColor = backgroundColorButton.getText();
        String txtColor = textColorButton.getText();
        String fontFace = fontComboBox.getSelectedItem().toString();
        int fontSize = Integer.parseInt(fontSizeComboBox.getSelectedItem().toString());
        int margin = Integer.parseInt(borderMarginComboBox.getSelectedItem().toString());

        String docStart = ChapterEditor.docStart(bgColor, txtColor, fontFace, fontSize, margin);
        String docEnd = ChapterEditor.docEnd();
        String txt = Utils.html2text(chapterEditor.getDocumentText());
        chapterEditor.getDocumentPane().setDocumentText(Utils.text2html(txt, docStart, docEnd));
        SwingUtilities.invokeLater(() -> {
            chapterEditor.getEditorPane().requestFocus();
            chapterEditor.getEditorPane().setCaretColor(Utils.hex2Rgb(txtColor));
            chapterEditor.getEditorPane().getCaret().setBlinkRate(0);
        });

    }

    private void onOK() {
        frame.preferences.put(ChapterEditor.Prop.PROP_BG_COLOR.name(), backgroundColorButton.getText());
        frame.preferences.put(ChapterEditor.Prop.PROP_TXT_COLOR.name(), textColorButton.getText());
        frame.preferences.put(ChapterEditor.Prop.PROP_FONT_FACE.name(), fontComboBox.getSelectedItem().toString());
        frame.preferences.put(ChapterEditor.Prop.PROP_FONT_SIZE.name(), fontSizeComboBox.getSelectedItem().toString());
        frame.preferences.put(ChapterEditor.Prop.PROP_MARGIN.name(), borderMarginComboBox.getSelectedItem().toString());

        String oldLaf = frame.preferences.get(ChapterEditor.Prop.PROP_LAF.name(), null);
        String newLaf = themeComboBox.getSelectedItem().toString();

        if (!newLaf.equals(oldLaf)) {
            frame.preferences.put(ChapterEditor.Prop.PROP_LAF.name(), newLaf);
            Utils.setLAF(newLaf, frame);
        }
        if (frame.chapterEditor != null) {
            frame.chapterEditor.reset();
        }
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
