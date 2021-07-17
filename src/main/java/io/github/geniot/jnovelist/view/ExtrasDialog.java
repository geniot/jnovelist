package io.github.geniot.jnovelist.view;

import io.github.geniot.jnovelist.ChapterEditor;
import io.github.geniot.jnovelist.ExtrasButton;
import io.github.geniot.jnovelist.PlusButton;
import io.github.geniot.jnovelist.WrapLayout;
import io.github.geniot.jnovelist.model.Chapter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;

public class ExtrasDialog extends JDialog {
    public JPanel chaptersPanel;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel editorPanel;
    private JNovelistApplication frame;
    private ChapterEditor chapterEditor;

    public ExtrasDialog(List<Chapter> chapterList, JNovelistApplication f, String title) {
        this.frame = f;
        setTitle(title);
        setContentPane(contentPane);
        setModal(true);
        chaptersPanel.setLayout(new WrapLayout(WrapLayout.LEFT, 5, 5));
        getRootPane().setDefaultButton(buttonOK);

        if (chapterList.isEmpty()) {
            chapterList.add(new Chapter());
        }


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

        int chapterCounter = 1;
        ButtonGroup chaptersButtonGroup = new ButtonGroup();
        for (Chapter chapter : chapterList) {
            ExtrasButton chapterButton = new ExtrasButton(chapterCounter, chapter, chapterList, this);
            ++chapterCounter;
            chaptersButtonGroup.add(chapterButton);
            chaptersPanel.add(chapterButton);
        }
        PlusButton chapterPlusButton = new PlusButton();
        chapterPlusButton.addActionListener(e -> {
            Chapter newChapter = new Chapter();
            chapterList.add(newChapter);
            ExtrasButton chapterButton = new ExtrasButton(chaptersPanel.getComponentCount(), newChapter, chapterList, ExtrasDialog.this);
            chaptersButtonGroup.add(chapterButton);
            chaptersPanel.add(chapterButton, chaptersPanel.getComponentCount() - 1);
            chapterButton.doClick();
        });
        chaptersPanel.add(chapterPlusButton);

        selectChapter();

        pack();
        setLocationRelativeTo(frame);
    }

    public static void main(String[] args) {
        ExtrasDialog dialog = new ExtrasDialog(Arrays.asList(new Chapter[]{new Chapter()}), null, "Test");
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
        // add your code here
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
        contentPane.setLayout(new BorderLayout(0, 0));
        editorPanel = new JPanel();
        editorPanel.setLayout(new BorderLayout(0, 0));
        contentPane.add(editorPanel, BorderLayout.CENTER);
        editorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        chaptersPanel = new JPanel();
        chaptersPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        contentPane.add(chaptersPanel, BorderLayout.NORTH);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    public void setChapter(Chapter chapter) {
        if (chapterEditor == null) {
            chapterEditor = new ChapterEditor(chapter);
            editorPanel.removeAll();
            editorPanel.add(chapterEditor, BorderLayout.CENTER);
        } else {
            chapterEditor.setChapter(chapter);
        }
        invalidate();
        validate();
        repaint();
    }

    public void selectChapter() {
//        int selectedChapter = preferences.getInt(JNovelistApplication.Prop.SELECTED_CHAPTER.name(), 1);
        int selectedChapter = 1;
        if (selectedChapter > chaptersPanel.getComponentCount() - 1) {
            selectedChapter = chaptersPanel.getComponentCount() - 1;
        }
        ((ExtrasButton) chaptersPanel.getComponent(selectedChapter - 1)).doClick();
    }
}
