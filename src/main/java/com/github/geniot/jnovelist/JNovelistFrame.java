package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.actions.*;
import com.lightdev.app.shtm.SHTMLPanelImpl;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.*;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class JNovelistFrame extends JFrame {

    protected JButton loadNovel;
    public JButton unloadNovel;
    public JButton saveNovel;
    public JButton exportNovel;

    public JButton heroes;
    public JButton places;
    public JButton things;
    public JButton notes;
    public JButton images;

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
        exportNovel = Utils.makeNavigationButton("Export", Constants.EXPORT_NOVEL_ACTION_COMMAND, "Export", "Export");

        heroes = Utils.makeNavigationButton("Heros", Constants.HEROES_NOVEL_ACTION_COMMAND, "Heroes", "Heroes");
        places = Utils.makeNavigationButton("Places", Constants.PLACES_NOVEL_ACTION_COMMAND, "Places", "Places");
        things = Utils.makeNavigationButton("Things", Constants.THINGS_NOVEL_ACTION_COMMAND, "Things", "Things");
        notes = Utils.makeNavigationButton("Notes", Constants.NOTES_NOVEL_ACTION_COMMAND, "Notes", "Notes");
        images = Utils.makeNavigationButton("Images", Constants.IMAGES_NOVEL_ACTION_COMMAND, "Images", "Images");

        saveNovel = Utils.makeNavigationButton("Save", Constants.SAVE_NOVEL_ACTION_COMMAND, "Save", "Save");

        loadNovel.addActionListener(new LoadNovelAction(this));
        unloadNovel.addActionListener(new UnloadAction(this));
        exportNovel.addActionListener(new ExportAction(this));

        heroes.addActionListener(new DialogAction(this));
        places.addActionListener(new DialogAction(this));
        things.addActionListener(new DialogAction(this));
        notes.addActionListener(new DialogAction(this));
        images.addActionListener(new DialogAction(this));

        saveNovel.addActionListener(new SaveAction(this));

        saveNovel.setEnabled(false);

        toolBar.add(loadNovel);
        toolBar.add(saveNovel);
        toolBar.add(heroes);
        toolBar.add(places);
        toolBar.add(things);
        toolBar.add(notes);
        toolBar.add(images);
        toolBar.addSeparator(new Dimension(30, 10));
        toolBar.add(exportNovel);
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
        return "JNovelist" + (openFileName == null ? "" : " - " + getNovelName());
    }

    public String getNovelName(){
        return openFileName.substring(openFileName.lastIndexOf(File.separator) + 1, openFileName.length());
    }


    public void updateState() {
        if (openFileName == null) {
            if (dnDTabbedPane != null) {
                dnDTabbedPane.removeAll();
                getContentPane().remove(dnDTabbedPane);
                dnDTabbedPane = null;
                validate();
                repaint();
                SHTMLPanelImpl.pluginManager = null;
                System.gc();
            }
            unloadNovel.setEnabled(false);
            heroes.setEnabled(false);
            places.setEnabled(false);
            things.setEnabled(false);
            notes.setEnabled(false);
            images.setEnabled(false);

            saveNovel.setEnabled(false);
            exportNovel.setEnabled(false);
        } else {
            exportNovel.setEnabled(true);
            unloadNovel.setEnabled(true);
            heroes.setEnabled(true);
            places.setEnabled(true);
            things.setEnabled(true);
            notes.setEnabled(true);
            images.setEnabled(true);
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
        if (dnDTabbedPane.getSelectedComponent() != null && dnDTabbedPane.getSelectedComponent() instanceof DnDTabbedPane) {
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
                partStatusLabel.setText(" CHR: " + partChars + " / WDS: " + partWords);

                if (selPart.getSelectedComponent() instanceof ChapterEditor){
                    ChapterEditor chapterEditor = (ChapterEditor) selPart.getSelectedComponent();
                    statusLabel.setText(" CHR: " + chapterEditor.charsSpaces + " / WDS: " + chapterEditor.words);
                }
            }
        }
        allStatusLabel.setText(" CHR: " + chars + " / WDS: " + words);
    }

    public String getNovelText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dnDTabbedPane.getTabCount(); i++) {
            Component c = dnDTabbedPane.getComponentAt(i);
            if (c instanceof DnDTabbedPane) {
                DnDTabbedPane dnd = (DnDTabbedPane) c;
                for (int k = 0; k < dnd.getTabCount(); k++) {
                    Component o = dnd.getComponentAt(k);
                    if (o instanceof ChapterEditor) {
                        ChapterEditor editor = (ChapterEditor) o;
                        try {
                            String newText = Utils.html2text(editor.getDocumentText());
                            sb.append(k+1);
                            sb.append('\n');
                            sb.append(newText);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return sb.toString();
    }
}
