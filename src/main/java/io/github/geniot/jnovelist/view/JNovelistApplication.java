package io.github.geniot.jnovelist.view;

import io.github.geniot.jnovelist.*;
import io.github.geniot.jnovelist.actions.CommitterTask;
import io.github.geniot.jnovelist.actions.LoadNovelAction;
import io.github.geniot.jnovelist.actions.NovelAction;
import io.github.geniot.jnovelist.model.Chapter;
import io.github.geniot.jnovelist.model.JNovel;
import io.github.geniot.jnovelist.model.Part;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JNovelistApplication extends DesktopApplication {
    private JPanel contentPanel;
    private JButton loadButton;
    public JPanel partsPanel;
    public JPanel chaptersPanel;
    public JPanel editorPanel;
    private JPanel tabsPanel;
    public JButton unloadButton;
    private JButton gitButton;
    private JButton preferencesButton;
    private JButton infoButton;
    private JPanel toolbarPanel;
    private JButton exportButton;
    public JLabel leftStatus;
    public JLabel centerStatus;
    public JLabel rightStatus;
    private JButton heroesButton;
    private JButton placesButton;
    private JButton thingsButton;
    private JButton notesButton;
    private JPanel novelbarPanel;
    private JPanel statusPanel;

    public JNovel novel;
    public String path;
    public CommitterTask committerTask;
    public ChapterEditor chapterEditor;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        contentPanel.add(panel1, BorderLayout.CENTER);
        tabsPanel = new JPanel();
        tabsPanel.setLayout(new BorderLayout(0, 0));
        panel1.add(tabsPanel, BorderLayout.NORTH);
        partsPanel = new JPanel();
        partsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabsPanel.add(partsPanel, BorderLayout.NORTH);
        chaptersPanel = new JPanel();
        chaptersPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        tabsPanel.add(chaptersPanel, BorderLayout.SOUTH);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        panel1.add(panel2, BorderLayout.CENTER);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        editorPanel = new JPanel();
        editorPanel.setLayout(new BorderLayout(0, 0));
        panel2.add(editorPanel, BorderLayout.CENTER);
        statusPanel = new JPanel();
        statusPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        leftStatus = new JLabel();
        leftStatus.setHorizontalAlignment(2);
        leftStatus.setText(" ");
        statusPanel.add(leftStatus, BorderLayout.WEST);
        centerStatus = new JLabel();
        centerStatus.setHorizontalAlignment(0);
        centerStatus.setText(" ");
        statusPanel.add(centerStatus, BorderLayout.CENTER);
        rightStatus = new JLabel();
        rightStatus.setHorizontalAlignment(4);
        rightStatus.setText(" ");
        statusPanel.add(rightStatus, BorderLayout.EAST);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        contentPanel.add(panel3, BorderLayout.NORTH);
        toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel3.add(toolbarPanel, BorderLayout.WEST);
        loadButton = new JButton();
        loadButton.setFocusPainted(false);
        loadButton.setFocusable(false);
        loadButton.setIcon(new ImageIcon(getClass().getResource("/images/Load.png")));
        loadButton.setMaximumSize(new Dimension(40, 40));
        loadButton.setMinimumSize(new Dimension(40, 40));
        loadButton.setPreferredSize(new Dimension(40, 40));
        loadButton.setText("");
        toolbarPanel.add(loadButton);
        gitButton = new JButton();
        gitButton.setEnabled(false);
        gitButton.setFocusPainted(false);
        gitButton.setFocusable(false);
        gitButton.setIcon(new ImageIcon(getClass().getResource("/images/Save.png")));
        gitButton.setMaximumSize(new Dimension(40, 40));
        gitButton.setMinimumSize(new Dimension(40, 40));
        gitButton.setPreferredSize(new Dimension(40, 40));
        gitButton.setText("");
        toolbarPanel.add(gitButton);
        exportButton = new JButton();
        exportButton.setBorderPainted(true);
        exportButton.setEnabled(true);
        exportButton.setFocusPainted(false);
        exportButton.setFocusable(false);
        exportButton.setIcon(new ImageIcon(getClass().getResource("/images/Export.png")));
        exportButton.setMaximumSize(new Dimension(40, 40));
        exportButton.setMinimumSize(new Dimension(40, 40));
        exportButton.setPreferredSize(new Dimension(40, 40));
        exportButton.setRequestFocusEnabled(false);
        exportButton.setText("");
        toolbarPanel.add(exportButton);
        unloadButton = new JButton();
        unloadButton.setEnabled(false);
        unloadButton.setFocusPainted(false);
        unloadButton.setFocusable(false);
        unloadButton.setIcon(new ImageIcon(getClass().getResource("/images/Unload.png")));
        unloadButton.setMaximumSize(new Dimension(40, 40));
        unloadButton.setMinimumSize(new Dimension(40, 40));
        unloadButton.setPreferredSize(new Dimension(40, 40));
        unloadButton.setText("");
        toolbarPanel.add(unloadButton);
        preferencesButton = new JButton();
        preferencesButton.setBorderPainted(true);
        preferencesButton.setEnabled(true);
        preferencesButton.setFocusPainted(false);
        preferencesButton.setFocusable(false);
        preferencesButton.setIcon(new ImageIcon(getClass().getResource("/images/Style.png")));
        preferencesButton.setMaximumSize(new Dimension(40, 40));
        preferencesButton.setMinimumSize(new Dimension(40, 40));
        preferencesButton.setPreferredSize(new Dimension(40, 40));
        preferencesButton.setRequestFocusEnabled(false);
        preferencesButton.setText("");
        toolbarPanel.add(preferencesButton);
        infoButton = new JButton();
        infoButton.setBorderPainted(true);
        infoButton.setEnabled(true);
        infoButton.setFocusPainted(false);
        infoButton.setFocusable(false);
        infoButton.setIcon(new ImageIcon(getClass().getResource("/images/Info.png")));
        infoButton.setMaximumSize(new Dimension(40, 40));
        infoButton.setMinimumSize(new Dimension(40, 40));
        infoButton.setPreferredSize(new Dimension(40, 40));
        infoButton.setRequestFocusEnabled(false);
        infoButton.setText("");
        toolbarPanel.add(infoButton);
        novelbarPanel = new JPanel();
        novelbarPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel3.add(novelbarPanel, BorderLayout.EAST);
        heroesButton = new JButton();
        heroesButton.setBorderPainted(true);
        heroesButton.setEnabled(true);
        heroesButton.setFocusPainted(false);
        heroesButton.setFocusable(false);
        heroesButton.setIcon(new ImageIcon(getClass().getResource("/images/Heros.png")));
        heroesButton.setMaximumSize(new Dimension(40, 40));
        heroesButton.setMinimumSize(new Dimension(40, 40));
        heroesButton.setPreferredSize(new Dimension(40, 40));
        heroesButton.setRequestFocusEnabled(false);
        heroesButton.setText("");
        novelbarPanel.add(heroesButton);
        placesButton = new JButton();
        placesButton.setBorderPainted(true);
        placesButton.setEnabled(true);
        placesButton.setFocusPainted(false);
        placesButton.setFocusable(false);
        placesButton.setIcon(new ImageIcon(getClass().getResource("/images/Places.png")));
        placesButton.setMaximumSize(new Dimension(40, 40));
        placesButton.setMinimumSize(new Dimension(40, 40));
        placesButton.setPreferredSize(new Dimension(40, 40));
        placesButton.setRequestFocusEnabled(false);
        placesButton.setText("");
        novelbarPanel.add(placesButton);
        thingsButton = new JButton();
        thingsButton.setBorderPainted(true);
        thingsButton.setEnabled(true);
        thingsButton.setFocusPainted(false);
        thingsButton.setFocusable(false);
        thingsButton.setIcon(new ImageIcon(getClass().getResource("/images/Things.png")));
        thingsButton.setMaximumSize(new Dimension(40, 40));
        thingsButton.setMinimumSize(new Dimension(40, 40));
        thingsButton.setPreferredSize(new Dimension(40, 40));
        thingsButton.setRequestFocusEnabled(false);
        thingsButton.setText("");
        novelbarPanel.add(thingsButton);
        notesButton = new JButton();
        notesButton.setBorderPainted(true);
        notesButton.setEnabled(true);
        notesButton.setFocusPainted(false);
        notesButton.setFocusable(false);
        notesButton.setIcon(new ImageIcon(getClass().getResource("/images/Notes.png")));
        notesButton.setMaximumSize(new Dimension(40, 40));
        notesButton.setMinimumSize(new Dimension(40, 40));
        notesButton.setPreferredSize(new Dimension(40, 40));
        notesButton.setRequestFocusEnabled(false);
        notesButton.setText("");
        novelbarPanel.add(notesButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }

    public enum Prop {
        SELECTED_PART,
        SELECTED_CHAPTER
    }

    protected LoadNovelAction loadNovelAction;

    public JNovelistApplication() {
        super();
        committerTask = new CommitterTask(this);
        scheduler.scheduleAtFixedRate(committerTask, 5, 5, TimeUnit.SECONDS);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        toolbarPanel.setLayout(new WrapLayout(WrapLayout.LEFT, 5, 5));
        novelbarPanel.setLayout(new WrapLayout(WrapLayout.LEFT, 5, 5));
        chaptersPanel.setLayout(new WrapLayout(WrapLayout.LEFT, 5, 5));
        partsPanel.setLayout(new WrapLayout(WrapLayout.LEFT, 5, 5));

        tabsPanel.setVisible(false);

        loadNovelAction = new LoadNovelAction(this);
        loadButton.addActionListener(loadNovelAction);

        unloadButton.addActionListener(e -> {
            chapterEditor = null;
            updateStatus();
            preferences.remove(LoadNovelAction.Prop.LAST_OPEN_FILE.name());
            committerTask.save(true, true);
        });

        gitButton.addActionListener(e -> committerTask.save(true, false));

        preferencesButton.addActionListener(e -> new PreferencesDialog(JNovelistApplication.this).setVisible(true));

        infoButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(URI.create("https://github.com/geniot/jnovelist"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ExportDialog(JNovelistApplication.this).setVisible(true);
            }
        });

        heroesButton.addActionListener(new NovelAction(this, NovelAction.Type.HEROES));
        placesButton.addActionListener(new NovelAction(this, NovelAction.Type.PLACES));
        thingsButton.addActionListener(new NovelAction(this, NovelAction.Type.THINGS));
        notesButton.addActionListener(new NovelAction(this, NovelAction.Type.NOTES));

        pack();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadNovelAction.loadLast();
            }
        });
    }

    @Override
    public void onWindowClosing() {
        committerTask.save(true, true);
    }

    public void setNovel(JNovel n, String p) {
        this.novel = n;
        this.path = p;

        partsPanel.removeAll();
        chaptersPanel.removeAll();
        editorPanel.removeAll();

        if (novel == null) {
            setTitle("JNovelist");

            tabsPanel.setVisible(false);
            unloadButton.setEnabled(false);
            gitButton.setEnabled(false);
            exportButton.setEnabled(false);

            heroesButton.setEnabled(false);
            placesButton.setEnabled(false);
            thingsButton.setEnabled(false);
            notesButton.setEnabled(false);

        } else {
            setTitle(path);
            preferences.put(LoadNovelAction.Prop.LAST_OPEN_FILE.name(), path);

            tabsPanel.setVisible(true);
            unloadButton.setEnabled(true);
            gitButton.setEnabled(true);
            exportButton.setEnabled(true);

            heroesButton.setEnabled(true);
            placesButton.setEnabled(true);
            thingsButton.setEnabled(true);
            notesButton.setEnabled(true);

            int partCounter = 1;
            ButtonGroup partsButtonGroup = new ButtonGroup();
            for (Part part : novel.getParts()) {
                PartButton partButton = new PartButton(partCounter, part, novel, this);
                ++partCounter;
                partsButtonGroup.add(partButton);
                partsPanel.add(partButton);
            }
            PlusButton partPlusButton = new PlusButton();
            partsPanel.add(partPlusButton);
            partPlusButton.addActionListener(e -> {
                Part newPart = new Part();
                novel.getParts().add(newPart);
                PartButton partButton = new PartButton(partsPanel.getComponentCount(), newPart, novel, JNovelistApplication.this);
                partsButtonGroup.add(partButton);
                partsPanel.add(partButton, partsPanel.getComponentCount() - 1);
                partButton.doClick();
            });
            selectPart();
        }
    }

    public void setPart(Part part) {
        chaptersPanel.removeAll();
        int chapterCounter = 1;
        ButtonGroup chaptersButtonGroup = new ButtonGroup();
        for (Chapter chapter : part.getChapters()) {
            ChapterButton chapterButton = new ChapterButton(chapterCounter, chapter, part, this);
            ++chapterCounter;
            chaptersButtonGroup.add(chapterButton);
            chaptersPanel.add(chapterButton);
        }
        PlusButton chapterPlusButton = new PlusButton();
        chapterPlusButton.addActionListener(e -> {
            Chapter newChapter = new Chapter();
            part.getChapters().add(newChapter);
            ChapterButton chapterButton = new ChapterButton(chaptersPanel.getComponentCount(), newChapter, part, JNovelistApplication.this);
            chaptersButtonGroup.add(chapterButton);
            chaptersPanel.add(chapterButton, chaptersPanel.getComponentCount() - 1);
            chapterButton.doClick();
        });
        chaptersPanel.add(chapterPlusButton);

        selectChapter();
    }

    public void setChapter(Chapter chapter) {
        if (chapterEditor == null) {
            chapterEditor = new ChapterEditor(chapter);
            chapterEditor.getDocument().addDocumentListener(new StatusUpdater(this));
            editorPanel.removeAll();
            editorPanel.add(chapterEditor, BorderLayout.CENTER);
        } else {
            chapterEditor.setChapter(chapter);
        }
        updateStatus();
        invalidate();
        validate();
        repaint();
    }


    public void updateStatus() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String displayType = preferences.get(ChapterEditor.Prop.PROP_STATS.name(), PreferencesDialog.Stats.CHARACTERS.label);
                if (displayType.equals(PreferencesDialog.Stats.NONE.label)) {
                    statusPanel.setVisible(false);
                } else {
                    statusPanel.setVisible(true);
                    if (chapterEditor == null) {
                        rightStatus.setText("");
                    } else {
                        if (displayType.equals(PreferencesDialog.Stats.CHARACTERS.label)) {
                            rightStatus.setText(String.valueOf(chapterEditor.chapter.sizeCharacters()));
                        } else if (displayType.equals(PreferencesDialog.Stats.WORDS.label)) {
                            rightStatus.setText(String.valueOf(chapterEditor.chapter.sizeWords()));
                        }
                    }
                }
            }
        });
    }

    public void selectChapter() {
        int selectedChapter = preferences.getInt(Prop.SELECTED_CHAPTER.name(), 1);
        if (selectedChapter > chaptersPanel.getComponentCount() - 1) {
            selectedChapter = chaptersPanel.getComponentCount() - 1;
        }
        ((ChapterButton) chaptersPanel.getComponent(selectedChapter - 1)).doClick();
    }

    public void selectPart() {
        int selectedPart = preferences.getInt(Prop.SELECTED_PART.name(), 1);
        if (selectedPart > partsPanel.getComponentCount() - 1) {
            selectedPart = partsPanel.getComponentCount() - 1;
        }
        ((PartButton) partsPanel.getComponent(selectedPart - 1)).doClick();
    }
}
