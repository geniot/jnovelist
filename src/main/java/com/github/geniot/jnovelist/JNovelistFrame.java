package com.github.geniot.jnovelist;

import org.mapdb.DB;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
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


    protected JEditorPane editorPane;

    protected JButton loadNovel;
    protected JButton unloadNovel;
    protected JScrollPane scrollPane;
    protected LinePainter linePainter;

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

        //Add the ubiquitous "Hello World" label.
        editorPane = new JEditorPane("text/html", "");
        HTMLEditorKit cssKit = new HTMLEditorKit();
        editorPane.setEditorKit(cssKit);
        StyleSheet styleSheet = cssKit.getStyleSheet();
        styleSheet.addRule("body {font-family:verdana; margin: 15px; }");
        styleSheet.addRule("h1 {color: #800000;}");
        styleSheet.addRule("h2 {color: #008000;}");
        Document doc = cssKit.createDefaultDocument();
        editorPane.setDocument(doc);
        linePainter = new LinePainter(editorPane);

        final UndoManager undo = new UndoManager();

        // Listen for undo and redo events
        doc.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent evt) {
                undo.addEdit(evt.getEdit());
            }
        });

        // Create an undo action and add it to the text component
        editorPane.getActionMap().put("Undo",
                new AbstractAction("Undo") {
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            if (undo.canUndo()) {
                                undo.undo();
                            }
                        } catch (CannotUndoException e) {
                        }
                    }
                });

        // Bind the undo action to ctl-Z
        editorPane.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

        // Create a redo action and add it to the text component
        editorPane.getActionMap().put("Redo",
                new AbstractAction("Redo") {
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            if (undo.canRedo()) {
                                undo.redo();
                            }
                        } catch (CannotRedoException e) {
                        }
                    }
                });

        // Bind the redo action to ctl-Y
        editorPane.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");

        scrollPane = new JScrollPane(editorPane);

        DnDTabbedPane dnDTabbedPane = new DnDTabbedPane(DnDTabbedPane.DECIMAL_TO_ROMAN);

        getContentPane().add(dnDTabbedPane, BorderLayout.CENTER);
        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        updateState(null);

        //Display the window.
        setPreferredSize(new Dimension(600, 800));
    }

    protected void updateState(String fileName) {
        if (openDB == null) {
            editorPane.setText("");
            editorPane.setVisible(false);
            unloadNovel.setEnabled(false);
        } else {
            editorPane.setVisible(true);
            unloadNovel.setEnabled(true);
        }
        setTitle(getDynTitle(fileName));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                editorPane.requestFocus();

                if (openDB != null) {
                    Object vp = openDB.treeMap(Constants.COLLECTION_PROPS).get(Constants.PROP_VIEW_POS);
                    if (vp != null) {
                        scrollPane.getVerticalScrollBar().setValue((Integer) vp);
                    }
                }

            }
        });
    }

    private String getDynTitle(String fileName) {
        return "JNovelist" + (openDB == null ? "" : " - " + fileName);
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
