package com.github.geniot.jnovelist;

import org.mapdb.DB;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
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

        loadNovel = makeNavigationButton("Load", LOAD_NOVEL, "Load", "Load");
        unloadNovel = makeNavigationButton("Eject", UNLOAD_NOVEL, "Unload", "Unload");

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

        JScrollPane scrollPane = new JScrollPane(editorPane);

        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(toolBar, BorderLayout.PAGE_START);

        updateState(null);

        //Display the window.
        setPreferredSize(new Dimension(600, 800));
    }

    protected void updateState(String fileName) {
        if (openDB == null) {
            editorPane.setVisible(false);
            unloadNovel.setEnabled(false);
        } else {
            editorPane.setVisible(true);
            unloadNovel.setEnabled(true);
        }
        setTitle(getDynTitle(fileName));
        editorPane.requestFocus();
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
