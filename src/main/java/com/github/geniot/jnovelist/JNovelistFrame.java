package com.github.geniot.jnovelist;

import org.mapdb.DB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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


    protected DB openDB;

    public JNovelistFrame() {
        super("JNovelist");

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (openDB != null) {
                    unloadNovel.doClick();
                }
                e.getWindow().dispose();
            }
        });

        JToolBar toolBar = new JToolBar("Still draggable");
        toolBar.setFloatable(false);

        loadNovel = makeNavigationButton("Load", Constants.LOAD_NOVEL_ACTION_COMMAND, "Load", "Load");
        unloadNovel = makeNavigationButton("Eject", Constants.UNLOAD_NOVEL_ACTION_COMMAND, "Unload", "Unload");

        loadNovel.addActionListener(new LoadNovelActionListener(this));
        unloadNovel.addActionListener(new UnloadActionListener(this));

        toolBar.add(loadNovel);
        toolBar.add(unloadNovel);


        getContentPane().add(toolBar, BorderLayout.PAGE_START);


        //Display the window.
        setPreferredSize(new Dimension(600, 800));

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (dnDTabbedPane != null) {
                            boolean enableRemoval = (e.getModifiers() & KeyEvent.ALT_MASK) != 0;
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


        updateState(null);
    }


    private String getDynTitle(String fileName) {
        return "JNovelist" + (openDB == null ? "" : " - " + fileName);
    }


    protected void updateState(String fileName) {
        if (openDB == null) {
            if (dnDTabbedPane != null) {
                getContentPane().remove(dnDTabbedPane);
                dnDTabbedPane = null;
                validate();
                repaint();

            }
            unloadNovel.setEnabled(false);
        } else {
            unloadNovel.setEnabled(true);
        }
        setTitle(getDynTitle(fileName));

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
