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
    protected LinePainter linePainter;

    public String absolutePathToTextFile;

    public int charsSpaces = 0;
    public int charsNoSpaces = 0;
    public int words = 0;

    public ChapterEditor(File file) {

        linePainter = new LinePainter(this.getDocumentPane().getEditor());

        if (file != null) {
            absolutePathToTextFile = file.getAbsolutePath();
            try {
                getDocumentPane().setDocumentText(Utils.text2html(FileUtils.readFileToString(file, "UTF-8")));
            } catch (Exception ex) {
                getDocumentPane().setDocumentText(ExceptionUtils.getFullStackTrace(ex));
            }

//            getDocumentPane().getEditor().setCaretPosition(ch.getCaretPosition() > getDocument().getLength() ? getDocument().getLength() : ch.getCaretPosition());
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
        String s = Utils.html2text(getDocumentText());
        String str = s.replaceAll("[!?,]", "");
        String[] wds = str.split("\\s+");
        String chars = s.replaceAll("\\s+", "");
        charsSpaces = s.length();
        charsNoSpaces = chars.length();
        words = wds.length == 1 && wds[0].equals("") ? 0 : wds.length;

        Utils.updateStatus(this);
    }

}
