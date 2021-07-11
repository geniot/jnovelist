package io.github.geniot.jnovelist.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.geniot.jnovelist.JNovelistApplication;
import io.github.geniot.jnovelist.model.JNovel;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class LoadNovelAction extends AbstractNovelistAction implements ActionListener {

    public enum Prop {
        LAST_OPEN_DIR,
        LAST_OPEN_FILE
    }

    public LoadNovelAction(JNovelistApplication f) {
        super(f);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File folder = new File(frame.preferences.get(Prop.LAST_OPEN_DIR.name(), System.getProperty("user.home")));
        JFileChooser fc = null;
        if (folder.exists()) {
            fc = new JFileChooser(folder);
        } else {
            fc = new JFileChooser(System.getProperty("user.home"));
        }
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".json") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "JNovelist project file in JSON format";
            }
        });

        int returnVal = fc.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            frame.preferences.put(Prop.LAST_OPEN_DIR.name(), fc.getCurrentDirectory().toString());
            File selectedFile = fc.getSelectedFile();
            loadNovel(selectedFile);

        } else if (returnVal == JFileChooser.CANCEL_OPTION) {
            frame.preferences.put(Prop.LAST_OPEN_DIR.name(), fc.getCurrentDirectory().toString());
        }
    }

    public void loadNovel(File selectedFile) {
        try {
            JNovel novel = null;
            if (!selectedFile.exists()) {
                String suffix = selectedFile.getAbsolutePath().endsWith(".json") ? "" : ".json";
                String path = selectedFile.getAbsolutePath() + suffix;
                frame.setNovel(new JNovel(), path);
            } else {
                String projectJson = FileUtils.readFileToString(selectedFile);
                novel = new ObjectMapper().readValue(projectJson, JNovel.class);
                frame.setNovel(novel, selectedFile.getAbsolutePath());
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadLast() {
        String lastLoadedNovelFilePath = frame.preferences.get(Prop.LAST_OPEN_FILE.name(), null);
        if (lastLoadedNovelFilePath != null) {
            File lastLoadedNovelFile = new File(lastLoadedNovelFilePath);
            if (lastLoadedNovelFile.exists()) {
                loadNovel(lastLoadedNovelFile);
            } else {
                frame.setNovel(null, null);
            }
        } else {
            frame.setNovel(null, null);
        }
    }
}
