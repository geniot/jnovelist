package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.Stats;
import org.mapdb.DB;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 23/06/15
 */
public class JNovelistFrame extends JFrame {
    private static final String NEW_NOVEL = "NEW_NOVEL";
    private static final String LOAD_NOVEL = "LOAD_NOVEL";
    private static final String UNLOAD_NOVEL = "UNLOAD_NOVEL";

    protected JButton loadNovel;
    protected JButton unloadNovel;
    protected DnDTabbedPane dnDTabbedPane;
    protected JLabel statusLabel;


    protected DB openDB;
    protected String openFileName;
    protected Stats stats;

    public JNovelistFrame() {
        super("JNovelist");

        try {
            Constants.PROPS.load(new FileInputStream(System.getProperty("user.home") + File.separator + Constants.PROPS_FILE_NAME));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (openDB != null) {
                    Constants.PROPS.setProperty(Constants.PROP_LAST_OPEN_FILE, openFileName);
                    Constants.PROPS.setProperty(Constants.PROP_WIDTH, String.valueOf(getWidth()));
                    Constants.PROPS.setProperty(Constants.PROP_HEIGHT, String.valueOf(getHeight()));
                    Constants.PROPS.setProperty(Constants.PROP_POS_X, String.valueOf((int) getLocation().getX()));
                    Constants.PROPS.setProperty(Constants.PROP_POS_Y, String.valueOf((int) getLocation().getY()));
                    unloadNovel.doClick();
                }else{
                    Constants.PROPS.remove(Constants.PROP_LAST_OPEN_FILE);
                }
                try {
                    FileOutputStream fos = new FileOutputStream(System.getProperty("user.home") + File.separator + Constants.PROPS_FILE_NAME);
                    Constants.PROPS.store(fos, "");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                e.getWindow().dispose();
            }
        });

        JToolBar toolBar = new JToolBar("Still draggable");
        toolBar.setFloatable(false);

        loadNovel = makeNavigationButton("Load", Constants.LOAD_NOVEL_ACTION_COMMAND, "Load", "Load");
        unloadNovel = makeNavigationButton("Eject", Constants.UNLOAD_NOVEL_ACTION_COMMAND, "Unload", "Unload");

        loadNovel.addActionListener(new LoadNovelAction(this));
        unloadNovel.addActionListener(new UnloadAction(this));

        toolBar.add(loadNovel);
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
        try{
            int width = Constants.PROPS.containsKey(Constants.PROP_WIDTH) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_WIDTH)) : 600;
            int height = Constants.PROPS.containsKey(Constants.PROP_HEIGHT) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_HEIGHT)) : 800;
            setPreferredSize(new Dimension(width, height));
        }catch (Exception ex){
            setPreferredSize(new Dimension(600, 800));
        }

        try {
            int posX = Constants.PROPS.containsKey(Constants.PROP_POS_X) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_POS_X)) : 0;
            int posY = Constants.PROPS.containsKey(Constants.PROP_POS_Y) ? Integer.parseInt(Constants.PROPS.getProperty(Constants.PROP_POS_Y)) : 0;
            setLocation(posX, posY);
        } catch (Exception ex) {
            setLocation(0, 0);
        }


        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    private boolean isEnabled;

                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        boolean enableRemoval = (e.getModifiers() & KeyEvent.ALT_MASK) != 0;
                        if (enableRemoval != isEnabled && dnDTabbedPane != null) {
                            isEnabled = enableRemoval;
                            for (int i = 0; i < dnDTabbedPane.getTabCount(); i++) {
                                Component c = dnDTabbedPane.getTabComponentAt(i);
                                if (c instanceof ButtonTabComponent) {
                                    ButtonTabComponent btc = (ButtonTabComponent) c;
                                    btc.enableRemoval(enableRemoval);
                                }
                                Component bookPartTab = dnDTabbedPane.getComponentAt(i);
                                if (bookPartTab instanceof DnDTabbedPane) {
                                    DnDTabbedPane dnd = (DnDTabbedPane) bookPartTab;
                                    for (int k = 0; k < dnd.getTabCount(); k++) {
                                        Component o = dnd.getTabComponentAt(k);
                                        if (o instanceof ButtonTabComponent) {
                                            ButtonTabComponent btc = (ButtonTabComponent) o;
                                            btc.enableRemoval(enableRemoval);
                                        }
                                    }
                                }
                            }
                        }
                        return false;
                    }
                });


        updateState();

        if (Constants.PROPS.containsKey(Constants.PROP_LAST_OPEN_FILE)) {
            String lastOpenFile = Constants.PROPS.getProperty(Constants.PROP_LAST_OPEN_FILE);
            File f = new File(lastOpenFile);
            if (f.exists()) {
                LoadNovelAction.loadNovel(this, f);
            }
        }
    }


    private String getDynTitle() {
        return "JNovelist" + (openDB == null ? "" : " - " + openFileName.substring(openFileName.lastIndexOf(File.separator) + 1, openFileName.length()));
    }


    protected void updateState() {
        if (openDB == null) {
            if (dnDTabbedPane != null) {
                getContentPane().remove(dnDTabbedPane);
                dnDTabbedPane = null;
                validate();
                repaint();
            }
            unloadNovel.setEnabled(false);
        } else {
            if (stats!=null){
                statusLabel.setText(stats.getStatus());
            }
            unloadNovel.setEnabled(true);
        }
        setTitle(getDynTitle());

//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                editorPane.requestFocus();
//
//                if (openDB != null) {
//                    Object vp = openDB.treeMap(Constants.COLLECTION_PROPS).get(Constants.PROP_VIEW_POS);
//                    if (vp != null) {
//                        scrollPane.getVerticalScrollBar().setValue((Integer) vp);
//                    }
//                }
//
//            }
//        });
    }

    protected JButton makeNavigationButton(String imageName,
                                           String actionCommand,
                                           String toolTipText,
                                           String altText) {
        //Look for the image.
        String imgLocation = "/images/" + imageName + ".png";
        URL imageURL = JNovelistLauncher.class.getResource(imgLocation);

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);

        if (imageURL != null) {                      //image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {                                     //no image found
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }

        return button;
    }

}
