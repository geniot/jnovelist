package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.model.Chapter;
import io.github.geniot.shtml.SHTMLPanelSingleDocImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ChapterEditor extends SHTMLPanelSingleDocImpl {
    private JNovelistApplication frame;
    private Chapter chapter;

    enum Prop {
        PROP_BG_COLOR,
        PROP_TXT_COLOR,
        PROP_FONT_FACE,
        PROP_FONT_SIZE,
        PROP_MARGIN
    }

    public String docStart() {
        String bgColor = frame.preferences.get(Prop.PROP_BG_COLOR.name(), "#FFFFFF");
        String txtColor = frame.preferences.get(Prop.PROP_TXT_COLOR.name(), "#000000");
        String fontFace = frame.preferences.get(Prop.PROP_FONT_FACE.name(), "Arial");
        int fontSize = frame.preferences.getInt(Prop.PROP_FONT_SIZE.name(), 12);
        int margin = frame.preferences.getInt(Prop.PROP_MARGIN.name(), 10);

        return "<html><head><style>body {\n" +
                "background-color: " + bgColor + "; \n" +
                "color: " + txtColor + "; \n" +
                "font-size:" + fontSize + "pt; \n" +
                "font-family:" + fontFace + "; \n" +
                "margin: " + margin + "px;\n" +
                "}</style></head><body>";
    }

    public String docEnd() {
        return "</body></html>";
    }

    public ChapterEditor(Chapter c, JNovelistApplication f) {
        super(true);
        this.frame = f;
        this.chapter = c;
        fixBorder();
        getEditorPane().getCaret().setBlinkRate(0);

        String str = String.join("\n", chapter.getLines());
        getDocumentPane().setDocumentText(Utils.text2html(str, docStart(), docEnd()));

        getDocumentPane().getEditor().getCaret().setBlinkRate(0);

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateChapter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateChapter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateChapter();
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                getEditorPane().requestFocus();
            }
        });
    }

    private void updateChapter() {
        String text = Utils.html2text(getDocumentText());
        chapter.setLines(text.split("\n"));
    }


    public void setChapter(Chapter c) {
        updateChapter();
        //detaching
        chapter = c;
        String str = String.join("\n", chapter.getLines());
        getDocumentPane().setDocumentText(Utils.text2html(str, docStart(), docEnd()));
    }

    private void fixBorder() {
        getDocumentPane().getEditor().setBorder(new EmptyBorder(0, 0, 0, 0));
        JViewport viewport = (JViewport) getDocumentPane().getEditor().getParent();
        JScrollPane scrollPane = (JScrollPane) viewport.getParent();
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
    }
}
