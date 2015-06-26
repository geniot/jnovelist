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
    public JButton saveNovel;
    public DnDTabbedPane dnDTabbedPane;
    public JLabel statusLabel;

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
        saveNovel = Utils.makeNavigationButton("Save", Constants.SAVE_NOVEL_ACTION_COMMAND, "Save", "Save");

        loadNovel.addActionListener(new LoadNovelAction(this));
        unloadNovel.addActionListener(new UnloadAction(this));
        saveNovel.addActionListener(new SaveAction(this));

        saveNovel.setEnabled(false);

        toolBar.add(loadNovel);
        toolBar.add(saveNovel);
        toolBar.add(unloadNovel);

        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 30));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel();
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);


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
            saveNovel.setEnabled(false);
        } else {
            unloadNovel.setEnabled(true);
        }
        setTitle(getDynTitle());
    }


    public void updateStatus() {
        saveNovel.setEnabled(true);
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
        statusLabel.setText(" CAL: " + chars + " / CNS: " + charsNoSpaces + " / WDS: " + words);
    }
}
