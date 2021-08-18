package io.github.geniot.jnovelist.view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import io.github.geniot.jnovelist.ChapterEditor;
import io.github.geniot.jnovelist.DesktopApplication;
import io.github.geniot.jnovelist.JNovelPreferences;
import io.github.geniot.jnovelist.Utils;
import io.github.geniot.jnovelist.model.Chapter;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PreferencesDialog extends JDialog {
    private static String LOREM_IPSUM;

    static {
        try {
            LOREM_IPSUM = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("lorem_ipsum.txt"), StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
    private JComboBox statsComboBox;
    private JNovelistApplication frame;
    private ChapterEditor chapterEditor;

    public PreferencesDialog(JNovelistApplication f) {
        this.frame = f;
        setContentPane(contentPane);
        setModal(true);
        setIconImage(DesktopApplication.ICON);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Preferences");
        setPreferredSize(new Dimension(600, 600));
        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontComboBox.setModel(new DefaultComboBoxModel(fonts));

        Chapter chapter = new Chapter();
        chapter.setLines(new String[]{LOREM_IPSUM});
        chapterEditor = new ChapterEditor(chapter);
        chapterEditor.setBorder(new LineBorder(Color.BLACK));
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

        backgroundColorButton.setText(JNovelPreferences.get(ChapterEditor.Prop.PROP_BG_COLOR.name(), "#FFFFFF"));

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

        String txtColor = JNovelPreferences.get(ChapterEditor.Prop.PROP_TXT_COLOR.name(), "#000000");
        textColorButton.setText(txtColor);
        chapterEditor.getEditorPane().setCaretColor(Utils.hex2Rgb(txtColor));


        fontComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        });
        fontComboBox.setSelectedItem(JNovelPreferences.get(ChapterEditor.Prop.PROP_FONT_FACE.name(), "Arial"));

        fontSizeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        });
        fontSizeComboBox.setSelectedItem(String.valueOf(JNovelPreferences.getInt(ChapterEditor.Prop.PROP_FONT_SIZE.name(), 12)));

        borderMarginComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        });
        borderMarginComboBox.setSelectedItem(String.valueOf(JNovelPreferences.getInt(ChapterEditor.Prop.PROP_MARGIN.name(), 10)));

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

        themeComboBox.setSelectedItem(JNovelPreferences.get(ChapterEditor.Prop.PROP_LAF.name(), "Luna"));

        statsComboBox.setSelectedItem(JNovelPreferences.get(ChapterEditor.Prop.PROP_STATS.name(), Stats.CHARACTERS.label));

        pack();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
    }

    public static void main(String[] args) {
        PreferencesDialog dialog = new PreferencesDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
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
        JNovelPreferences.put(ChapterEditor.Prop.PROP_BG_COLOR.name(), backgroundColorButton.getText());
        JNovelPreferences.put(ChapterEditor.Prop.PROP_TXT_COLOR.name(), textColorButton.getText());
        JNovelPreferences.put(ChapterEditor.Prop.PROP_FONT_FACE.name(), fontComboBox.getSelectedItem().toString());
        JNovelPreferences.put(ChapterEditor.Prop.PROP_FONT_SIZE.name(), fontSizeComboBox.getSelectedItem().toString());
        JNovelPreferences.put(ChapterEditor.Prop.PROP_MARGIN.name(), borderMarginComboBox.getSelectedItem().toString());
        JNovelPreferences.put(ChapterEditor.Prop.PROP_STATS.name(), statsComboBox.getSelectedItem().toString());

        String oldLaf = JNovelPreferences.get(ChapterEditor.Prop.PROP_LAF.name(), null);
        String newLaf = themeComboBox.getSelectedItem().toString();

        if (!newLaf.equals(oldLaf)) {
            JNovelPreferences.put(ChapterEditor.Prop.PROP_LAF.name(), newLaf);
            Utils.setLAF(newLaf, frame);
        }
        if (frame.chapterEditor != null) {
            frame.updateStatus();
            frame.chapterEditor.reset();
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setFocusPainted(false);
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setFocusPainted(false);
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(8, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Theme:");
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        themeComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Acryl");
        defaultComboBoxModel1.addElement("Aero");
        defaultComboBoxModel1.addElement("Aluminium");
        defaultComboBoxModel1.addElement("Bernstein");
        defaultComboBoxModel1.addElement("Fast");
        defaultComboBoxModel1.addElement("Graphite");
        defaultComboBoxModel1.addElement("HiFi");
        defaultComboBoxModel1.addElement("Luna");
        defaultComboBoxModel1.addElement("McWin");
        defaultComboBoxModel1.addElement("Mint");
        defaultComboBoxModel1.addElement("Noire");
        defaultComboBoxModel1.addElement("Smart");
        defaultComboBoxModel1.addElement("Texture");
        themeComboBox.setModel(defaultComboBoxModel1);
        panel3.add(themeComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Background color:");
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backgroundColorButton = new JButton();
        backgroundColorButton.setFocusPainted(false);
        backgroundColorButton.setText("#FFFFFF");
        panel3.add(backgroundColorButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Text color:");
        panel3.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textColorButton = new JButton();
        textColorButton.setFocusPainted(false);
        textColorButton.setText("#000000");
        panel3.add(textColorButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Font family:");
        panel3.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fontComboBox = new JComboBox();
        panel3.add(fontComboBox, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Font size:");
        panel3.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fontSizeComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        defaultComboBoxModel2.addElement("8");
        defaultComboBoxModel2.addElement("9");
        defaultComboBoxModel2.addElement("10");
        defaultComboBoxModel2.addElement("11");
        defaultComboBoxModel2.addElement("12");
        defaultComboBoxModel2.addElement("14");
        defaultComboBoxModel2.addElement("16");
        defaultComboBoxModel2.addElement("18");
        defaultComboBoxModel2.addElement("20");
        defaultComboBoxModel2.addElement("22");
        defaultComboBoxModel2.addElement("24");
        defaultComboBoxModel2.addElement("26");
        defaultComboBoxModel2.addElement("28");
        defaultComboBoxModel2.addElement("36");
        defaultComboBoxModel2.addElement("48");
        defaultComboBoxModel2.addElement("72");
        fontSizeComboBox.setModel(defaultComboBoxModel2);
        panel3.add(fontSizeComboBox, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Border margin:");
        panel3.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        borderMarginComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
        defaultComboBoxModel3.addElement("0");
        defaultComboBoxModel3.addElement("5");
        defaultComboBoxModel3.addElement("10");
        defaultComboBoxModel3.addElement("15");
        defaultComboBoxModel3.addElement("20");
        defaultComboBoxModel3.addElement("25");
        defaultComboBoxModel3.addElement("30");
        defaultComboBoxModel3.addElement("35");
        defaultComboBoxModel3.addElement("40");
        defaultComboBoxModel3.addElement("45");
        defaultComboBoxModel3.addElement("50");
        defaultComboBoxModel3.addElement("60");
        defaultComboBoxModel3.addElement("70");
        defaultComboBoxModel3.addElement("80");
        defaultComboBoxModel3.addElement("90");
        borderMarginComboBox.setModel(defaultComboBoxModel3);
        panel3.add(borderMarginComboBox, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(7, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Editor Preview", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        previewPanel = new JPanel();
        previewPanel.setLayout(new BorderLayout(0, 0));
        panel4.add(previewPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        previewPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label7 = new JLabel();
        label7.setText("Status bar stats:");
        panel3.add(label7, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statsComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
        defaultComboBoxModel4.addElement("none");
        defaultComboBoxModel4.addElement("characters");
        defaultComboBoxModel4.addElement("words");
        statsComboBox.setModel(defaultComboBoxModel4);
        panel3.add(statsComboBox, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    public enum Stats {
        NONE("none"),
        CHARACTERS("characters"),
        WORDS("words");
        public final String label;

        private Stats(String label) {
            this.label = label;
        }
    }

}
