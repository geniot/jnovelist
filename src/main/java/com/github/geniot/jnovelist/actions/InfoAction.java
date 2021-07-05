package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.ChapterEditor;
import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 20/07/16
 */
public class InfoAction extends AbstractNovelistAction {

    public InfoAction(JNovelistFrame f) {
        super(f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Desktop.getDesktop().browse(URI.create("https://github.com/geniot/jnovelist"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


}

