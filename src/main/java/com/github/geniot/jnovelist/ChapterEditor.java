package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.project.Scene;
import com.inet.jortho.SpellChecker;
import com.lightdev.app.shtm.SHTMLPanelSingleDocImpl;
import org.apache.commons.lang.exception.ExceptionUtils;

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


    public ChapterEditor(Scene scene, String docStart, String docEnd) {

//        linePainter = new LinePainter(this.getDocumentPane().getEditor());

        if (scene != null) {
            try {
                String str = scene.getContent();//FileUtils.readFileToString(file, "UTF-8");
//                if (!str.contains(" ")){
//                    str = Utils.base64decode(str);
//                }
                getDocumentPane().setDocumentText(Utils.text2html(str, docStart, docEnd));
//                getDocumentPane().setDocumentText(Utils.text2html(FileUtils.readFileToString(file, "UTF-8")));
            } catch (Exception ex) {
                getDocumentPane().setDocumentText(ExceptionUtils.getFullStackTrace(ex));
            }

            int caretPos = scene.getCaretPos();
//            if (Constants.PROPS.getProperty("caretPosition:" + file.getAbsolutePath()) != null) {
//                caretPos = Integer.parseInt(Constants.PROPS.getProperty("caretPosition:" + file.getAbsolutePath()));
//            }
            getDocumentPane().getEditor().setCaretPosition(caretPos > getDocument().getLength() ? getDocument().getLength() : caretPos);
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
        SpellChecker.register(getDocumentPane().getEditor());

        updateStatus();
    }

    public ChapterEditor(String file, boolean editable, String docStart, String docEnd) {
        try {
            getDocumentPane().setDocumentText(Utils.text2html(file, docStart, docEnd));
        } catch (Exception ex) {
            getDocumentPane().setDocumentText(ExceptionUtils.getFullStackTrace(ex));
        }
        getEditorPane().setEditable(editable);
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
