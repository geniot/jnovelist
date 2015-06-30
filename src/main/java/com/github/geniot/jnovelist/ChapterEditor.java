package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.Chapter;
import com.github.geniot.jnovelist.model.ITextable;
import com.lightdev.app.shtm.DocumentPane;
import com.lightdev.app.shtm.SHTMLPanelSingleDocImpl;
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
import java.awt.event.ActionEvent;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 24/06/15
 */
public class ChapterEditor extends SHTMLPanelSingleDocImpl {
    protected LinePainter linePainter;

    public int charsSpaces = 0;
    public int charsNoSpaces = 0;
    public int words = 0;

    public ChapterEditor(ITextable ch) {

        linePainter = new LinePainter(this.getDocumentPane().getEditor());

        if (ch != null) {
            getDocumentPane().setDocumentText(ch.getText());
            getDocumentPane().getEditor().setCaretPosition(ch.getCaretPosition() > getDocument().getLength() ? getDocument().getLength() : ch.getCaretPosition());
        } else {
            getDocumentPane().setDocumentText(Constants.EMPTY_DOC);
        }


        getDocument().addDocumentListener(new DocumentListener() {
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
        String s = Jsoup.parse(getDocumentText()).text();
        String str = s.replaceAll("[!?,]", "");
        String[] wds = str.split("\\s+");
        String chars = s.replaceAll("\\s+", "");
        charsSpaces = s.length();
        charsNoSpaces = chars.length();
        words = wds.length == 1 && wds[0].equals("") ? 0 : wds.length;

        Utils.updateStatus(this);
    }


    public Chapter getChapter() {
        Chapter ch = new Chapter();
        ch.setText(getDocumentText());
        return ch;
    }
}
