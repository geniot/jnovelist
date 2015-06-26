package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.Chapter;
import com.lightdev.app.shtm.DocumentPane;
import org.jsoup.Jsoup;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 24/06/15
 */
public class ChapterEditor extends DocumentPane {
    protected LinePainter linePainter;

    public int charsSpaces = 0;
    public int charsNoSpaces = 0;
    public int words = 0;

    public ChapterEditor(Chapter ch) {
//        super("text/html", "");

        //Add the ubiquitous "Hello World" label.
//        HTMLEditorKit cssKit = new HTMLEditorKit();
//        setEditorKit(cssKit);
//        StyleSheet styleSheet = cssKit.getStyleSheet();
//        styleSheet.addRule("body {font-family:verdana; margin: 15px; font-size:large }");
//        styleSheet.addRule("h1 {color: #800000;}");
//        styleSheet.addRule("h2 {color: #008000;}");
//        styleSheet.addRule("p {margin-bottom: 0;}");

        linePainter = new LinePainter(this.getEditor());

        if (ch != null) {
            setDocumentText(ch.getText());
            getEditor().setCaretPosition(ch.getCaretPosition() > getDocument().getLength() ? getDocument().getLength() : ch.getCaretPosition());
        } else {
            setDocumentText(Constants.EMPTY_DOC);
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
