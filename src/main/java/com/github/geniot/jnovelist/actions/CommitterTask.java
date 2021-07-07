package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.JNovelistFrame;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import static com.github.geniot.jnovelist.Utils.getGitRootDir;
import static com.github.geniot.jnovelist.Utils.runCommand;

public class CommitterTask implements Runnable {
    int projectHash;
    private final JNovelistFrame frame;
    private boolean isRunning = false;

    public CommitterTask(JNovelistFrame frame) {
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            if (frame.openFileName != null && !this.isRunning) {
                isRunning = true;
                int hash = frame.getNovelText().hashCode();
                if (hash != projectHash) {
                    frame.saveAction.save();
                    File f = getGitRootDir(frame.openFileName);
                    if (f != null) {
                        System.out.println(runCommand("git add -A", f));
                        System.out.println(runCommand("git commit -m \"" + System.currentTimeMillis() + "\"", f));
                        projectHash = hash;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            isRunning = false;
        }
    }
}
