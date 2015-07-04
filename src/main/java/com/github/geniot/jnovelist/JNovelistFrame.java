package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.actions.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class JNovelistFrame extends JFrame {

    protected JButton loadNovel;
    public JButton unloadNovel;
    //    public JButton saveNovel;
    public JButton heroes;
    public JButton places;
    public JButton things;
    public JButton notes;

    public DnDTabbedPane dnDTabbedPane;

    public JLabel statusLabel;
    public JLabel partStatusLabel;
    public JLabel allStatusLabel;

    public String openFileName;

    public JNovelistFrame() {
        super("JNovelist");

        try {
            Constants.PROPS.load(new FileInputStream(System.getProperty("user.home") + File.separator + Constants.PROPS_FILE_NAME));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new OnExitAction(this));

        JToolBar toolBar = new JToolBar("Still draggable");
        toolBar.setFloatable(false);

        loadNovel = Utils.makeNavigationButton("Load", Constants.LOAD_NOVEL_ACTION_COMMAND, "Load", "Load");
        unloadNovel = Utils.makeNavigationButton("Eject", Constants.UNLOAD_NOVEL_ACTION_COMMAND, "Unload", "Unload");

        heroes = Utils.makeNavigationButton("Heros", Constants.HEROES_NOVEL_ACTION_COMMAND, "Heroes", "Heroes");
        places = Utils.makeNavigationButton("Places", Constants.PLACES_NOVEL_ACTION_COMMAND, "Places", "Places");
        things = Utils.makeNavigationButton("Things", Constants.THINGS_NOVEL_ACTION_COMMAND, "Things", "Things");
        notes = Utils.makeNavigationButton("Notes", Constants.NOTES_NOVEL_ACTION_COMMAND, "Notes", "Notes");
//        saveNovel = Utils.makeNavigationButton("Save", Constants.SAVE_NOVEL_ACTION_COMMAND, "Save", "Save");

        loadNovel.addActionListener(new LoadNovelAction(this));
        unloadNovel.addActionListener(new UnloadAction(this));

        heroes.addActionListener(new DialogAction(this));
        places.addActionListener(new DialogAction(this));
        things.addActionListener(new DialogAction(this));
        notes.addActionListener(new DialogAction(this));
//        saveNovel.addActionListener(new SaveAction(this));

//        saveNovel.setEnabled(false);

        toolBar.add(loadNovel);
//        toolBar.add(saveNovel);
        toolBar.add(heroes);
        toolBar.add(places);
        toolBar.add(things);
        toolBar.add(notes);
        toolBar.addSeparator(new Dimension(30, 10));
        toolBar.add(unloadNovel);

        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 30));
        statusPanel.setLayout(new BorderLayout());

        statusLabel = new JLabel();
        statusPanel.add(statusLabel, BorderLayout.WEST);

        partStatusLabel = new JLabel();
        statusPanel.add(partStatusLabel, BorderLayout.CENTER);
        partStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        allStatusLabel = new JLabel();
        statusPanel.add(allStatusLabel, BorderLayout.EAST);


        //Display the window.
        try {
            int width = Constants.PROPS.containsKey(Constants.PROP_WIDTH) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_WIDTH)) : 600;
            int height = Constants.PROPS.containsKey(Constants.PROP_HEIGHT) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_HEIGHT)) : 800;
            setPreferredSize(new Dimension(width, height));
        } catch (Exception ex) {
            setPreferredSize(new Dimension(600, 800));
        }

        try {
            int posX = Constants.PROPS.containsKey(Constants.PROP_POS_X) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_POS_X)) : 0;
            int posY = Constants.PROPS.containsKey(Constants.PROP_POS_Y) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_POS_Y)) : 0;
            setLocation(posX, posY);
        } catch (Exception ex) {
            setLocation(0, 0);
        }


        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new SetRemovalStateAction(this));
        updateState();

        if (Constants.PROPS.containsKey(Constants.PROP_LAST_OPEN_FILE)) {
            String lastOpenFile = Constants.PROPS.getProperty(Constants.PROP_LAST_OPEN_FILE);
            final File f = new File(lastOpenFile);
            if (f.exists()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        LoadNovelAction.loadNovel(JNovelistFrame.this, f);
                    }
                });
            }
        }
    }


    private String getDynTitle() {
        return "JNovelist" + (openFileName == null ? "" : " - " + openFileName.substring(openFileName.lastIndexOf(File.separator) + 1, openFileName.length()));
    }


    public void updateState() {
        if (openFileName == null) {
            if (dnDTabbedPane != null) {
                getContentPane().remove(dnDTabbedPane);
                dnDTabbedPane = null;
                validate();
                repaint();
            }
            unloadNovel.setEnabled(false);
            heroes.setEnabled(false);
            places.setEnabled(false);
            things.setEnabled(false);
            notes.setEnabled(false);
        } else {
            unloadNovel.setEnabled(true);
            heroes.setEnabled(true);
            places.setEnabled(true);
            things.setEnabled(true);
            notes.setEnabled(true);
        }
        setTitle(getDynTitle());
        updateStatus();
    }


    public void updateStatus() {
        if (dnDTabbedPane == null) {
            partStatusLabel.setText("");
            statusLabel.setText("");
            allStatusLabel.setText("");
            return;
        }

//        saveNovel.setEnabled(true);

        int chars = 0;
        int charsNoSpaces = 0;
        int words = 0;

        for (int i = 0; i < dnDTabbedPane.getTabCount(); i++) {
            Component c = dnDTabbedPane.getComponentAt(i);
            if (c instanceof DnDTabbedPane) {
                DnDTabbedPane dnd = (DnDTabbedPane) c;
                for (int k = 0; k < dnd.getTabCount(); k++) {
                    Component o = dnd.getComponentAt(k);
                    if (o instanceof ChapterEditor) {
                        ChapterEditor editor = (ChapterEditor) o;
                        chars += editor.charsSpaces;
                        charsNoSpaces += editor.charsNoSpaces;
                        words += editor.words;
                    }
                }
            }
        }
        if (dnDTabbedPane.getSelectedComponent() != null) {
            DnDTabbedPane selPart = (DnDTabbedPane) dnDTabbedPane.getSelectedComponent();

            if (selPart.getSelectedComponent() != null) {
                int partChars = 0;
                int partCharsNoSpaces = 0;
                int partWords = 0;
                for (int k = 0; k < selPart.getTabCount(); k++) {
                    Component o = selPart.getComponentAt(k);
                    if (o instanceof ChapterEditor) {
                        ChapterEditor editor = (ChapterEditor) o;
                        partChars += editor.charsSpaces;
                        partCharsNoSpaces += editor.charsNoSpaces;
                        partWords += editor.words;
                    }
                }
                partStatusLabel.setText(" CAL: " + partChars + " / WDS: " + partWords);
                ChapterEditor chapterEditor = (ChapterEditor) selPart.getSelectedComponent();
                statusLabel.setText(" CAL: " + chapterEditor.charsSpaces + " / WDS: " + chapterEditor.words);

            }
        }
        allStatusLabel.setText(" CAL: " + chars + " / WDS: " + words);
    }
}
