package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.Chapter;
import org.jsoup.Jsoup;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 24/06/15
 */
public class ChapterEditor extends JEditorPane {
    protected LinePainter linePainter;

    public int charsSpaces = 0;
    public int charsNoSpaces = 0;
    public int words = 0;

    public ChapterEditor(Chapter ch) {
        super("text/html", "");

        //Add the ubiquitous "Hello World" label.
        HTMLEditorKit cssKit = new HTMLEditorKit();
        setEditorKit(cssKit);
        StyleSheet styleSheet = cssKit.getStyleSheet();
        styleSheet.addRule("body {font-family:verdana; margin: 15px; font-size:large }");
        styleSheet.addRule("h1 {color: #800000;}");
        styleSheet.addRule("h2 {color: #008000;}");
//        styleSheet.addRule("p {margin-bottom: 0;}");
        Document doc = cssKit.createDefaultDocument();
        setDocument(doc);
        linePainter = new LinePainter(this);

        if (ch != null) {
            setText(ch.getText());
            setCaretPosition(ch.getCaretPosition() > getDocument().getLength() ? getDocument().getLength() : ch.getCaretPosition());
        } else {
            setText("<html><head></head><body><p></p></body></html>");
        }

        final UndoManager undo = new UndoManager();

        // Listen for undo and redo events
        doc.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent evt) {
                undo.addEdit(evt.getEdit());
            }
        });

        // Create an undo action and add it to the text component
        getActionMap().put("Undo",
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
        getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

        // Create a redo action and add it to the text component
        getActionMap().put("Redo",
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
        getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");

        doc.addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStatus();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStatus();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStatus();
            }
        });

        updateStatus();
    }

    private void updateStatus() {
        String s = Jsoup.parse(getText()).text();
        String str = s.replaceAll("[!?,]", "");
        String[] wds = str.split("\\s+");
        String chars = s.replaceAll("\\s+", "");
        charsSpaces = s.length();
        charsNoSpaces = chars.length();
        words = wds.length == 1 && wds[0].equals("") ? 0 : wds.length;


        Container c = getParent();
        while (c != null && !(c instanceof JNovelistFrame)) {
            c = c.getParent();
        }
        if (c != null) {
            ((JNovelistFrame) c).updateStatus();
        }
    }


    public Chapter getChapter() {
        Chapter ch = new Chapter();
        String text = getText();
        ch.setText(text);
        return ch;
    }
}
