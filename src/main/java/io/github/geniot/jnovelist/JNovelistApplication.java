package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.actions.CommitterTask;
import io.github.geniot.jnovelist.actions.LoadNovelAction;
import io.github.geniot.jnovelist.model.Chapter;
import io.github.geniot.jnovelist.model.JNovel;
import io.github.geniot.jnovelist.model.Part;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JNovelistApplication extends DesktopApplication {
    private JPanel contentPanel;
    private JButton loadButton;
    public JPanel partsPanel;
    public JPanel chaptersPanel;
    private JPanel editorPanel;
    private JPanel tabsPanel;
    private JButton unloadButton;

    public JNovel novel;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    enum Prop {
        SELECTED_PART,
        SELECTED_CHAPTER
    }

    protected LoadNovelAction loadNovelAction;

    public JNovelistApplication() {
        super();
        scheduler.scheduleAtFixedRate(new CommitterTask(this), 5, 5, TimeUnit.SECONDS);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        chaptersPanel.setLayout(new WrapLayout());
        partsPanel.setLayout(new WrapLayout());

        tabsPanel.setVisible(false);

        loadNovelAction = new LoadNovelAction(this);
        loadButton.addActionListener(loadNovelAction);

        unloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNovel(null, null);
            }
        });

        pack();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadNovelAction.loadLast();
            }
        });
    }

    public void setNovel(JNovel n, String path) {
        this.novel = n;

        partsPanel.removeAll();
        chaptersPanel.removeAll();
        editorPanel.removeAll();

        if (novel == null) {
            setTitle("JNovelist");
            preferences.remove(LoadNovelAction.Prop.LAST_OPEN_FILE.name());
            tabsPanel.setVisible(false);
            unloadButton.setEnabled(false);
        } else {
            setTitle(path);
            preferences.put(LoadNovelAction.Prop.LAST_OPEN_FILE.name(), path);
            tabsPanel.setVisible(true);
            unloadButton.setEnabled(true);
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
        if (editorPanel.getComponentCount() == 0) {
            editorPanel.add(new ChapterEditor(chapter, this), BorderLayout.CENTER);
        } else {
            ChapterEditor chapterEditor = (ChapterEditor) editorPanel.getComponent(0);
            chapterEditor.setChapter(chapter);
        }
        invalidate();
        validate();
        repaint();
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
