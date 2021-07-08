package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.Chapter;
import io.github.geniot.shtml.SHTMLPanelSingleDocImpl;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 24/06/15
 */
public class ChapterEditor extends SHTMLPanelSingleDocImpl {
//    protected LinePainter linePainter;


    public int charsSpaces = 0;
    public int charsNoSpaces = 0;
    public int words = 0;


    public ChapterEditor(Chapter chapter, String docStart, String docEnd) {
        super(true);

        fixBorder();
        getEditorPane().getCaret().setBlinkRate(0);
//        linePainter = new LinePainter(this.getDocumentPane().getEditor());

        if (chapter != null) {
            try {
                String str = String.join("\n", chapter.getLines());//FileUtils.readFileToString(file, "UTF-8");
//                if (!str.contains(" ")){
//                    str = Utils.base64decode(str);
//                }
                getDocumentPane().setDocumentText(Utils.text2html(str, docStart, docEnd));
//                getDocumentPane().setDocumentText(Utils.text2html(FileUtils.readFileToString(file, "UTF-8")));
            } catch (Exception ex) {
                getDocumentPane().setDocumentText(ExceptionUtils.getFullStackTrace(ex));
            }

//            int caretPos = chapter.getCaretPos();
//            if (Constants.PROPS.getProperty("caretPosition:" + file.getAbsolutePath()) != null) {
//                caretPos = Integer.parseInt(Constants.PROPS.getProperty("caretPosition:" + file.getAbsolutePath()));
//            }
//            getDocumentPane().getEditor().setCaretPosition(caretPos > getDocument().getLength() ? getDocument().getLength() : caretPos);
            getDocumentPane().getEditor().getCaret().setBlinkRate(0);
        } else {
            getDocumentPane().setDocumentText(docStart + "<p></p>" + docEnd);
        }

        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                enableSave();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                enableSave();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                enableSave();
            }
        });

        // enable the spell checking on the text component with all features
//todo: enable and fix bugs
//        SpellChecker.register(getDocumentPane().getEditor());

        updateStatus();
    }

    public ChapterEditor(String file, boolean editable, String docStart, String docEnd) {
        super(true);
        fixBorder();
        try {
            getDocumentPane().setDocumentText(Utils.text2html(file, docStart, docEnd));
        } catch (Exception ex) {
            getDocumentPane().setDocumentText(ExceptionUtils.getFullStackTrace(ex));
        }
        getEditorPane().setEditable(editable);
    }

    private void fixBorder() {
        getDocumentPane().getEditor().setBorder(new EmptyBorder(0, 0, 0, 0));
        JViewport viewport = (JViewport) getDocumentPane().getEditor().getParent();
        JScrollPane scrollPane = (JScrollPane) viewport.getParent();
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    private void enableSave() {
        Utils.enableSave(this);
        updateStatus();
    }


    private void updateStatus() {
        String s = Utils.html2text(getDocumentText());
        String str = s.replaceAll("[!?,]", "");
        String[] wds = str.split("\\s+");
        String chars = s.replaceAll("\\s+", "");
        charsSpaces = s.replaceAll("\n", "").length();
        charsNoSpaces = chars.length();
        words = wds.length == 1 && wds[0].equals("") ? 0 : wds.length;

        Utils.updateStatus(this);
    }


}
