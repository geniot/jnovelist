package com.github.geniot.jnovelist;

import com.lightdev.app.shtm.SHTMLPanelSingleDocImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.File;


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


    public ChapterEditor(File file) {

//        linePainter = new LinePainter(this.getDocumentPane().getEditor());

        if (file != null) {
            try {
                getDocumentPane().setDocumentText(Utils.text2html(Utils.base64decode(FileUtils.readFileToString(file, "UTF-8"))));
//                getDocumentPane().setDocumentText(Utils.text2html(FileUtils.readFileToString(file, "UTF-8")));
            } catch (Exception ex) {
                getDocumentPane().setDocumentText(ExceptionUtils.getFullStackTrace(ex));
            }

            int caretPos = 0;
            if (Constants.PROPS.getProperty("caretPosition:" + file.getAbsolutePath()) != null) {
                caretPos = Integer.parseInt(Constants.PROPS.getProperty("caretPosition:" + file.getAbsolutePath()));
            }
            getDocumentPane().getEditor().setCaretPosition(caretPos > getDocument().getLength() ? getDocument().getLength() : caretPos);
        } else {
            getDocumentPane().setDocumentText(Constants.EMPTY_DOC);
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
        updateStatus();
    }

    public ChapterEditor(String file) {
        try {
            getDocumentPane().setDocumentText(Utils.text2html(file));
        } catch (Exception ex) {
            getDocumentPane().setDocumentText(ExceptionUtils.getFullStackTrace(ex));
        }
        getEditorPane().setEditable(false);
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
        charsSpaces = s.replaceAll("\n","").length();
        charsNoSpaces = chars.length();
        words = wds.length == 1 && wds[0].equals("") ? 0 : wds.length;

        Utils.updateStatus(this);
    }

}
