package io.github.geniot.jnovelist.actions;

import io.github.geniot.jnovelist.JNovelistApplication;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import static io.github.geniot.jnovelist.Utils.getGitRootDir;

public class PushTask extends SwingWorker<Void, Void> {

    JNovelistApplication frame;

    public enum Prop {
        LOG, STATUS, FINISHED
    }


    public PushTask(JNovelistApplication f) {
        this.frame = f;
    }

    /*
     * Main task. Executed in background thread.
     */
    @Override
    public Void doInBackground() {
        File f = getGitRootDir(frame.path);
        runCommand("git add -A", f, true);
        runCommand("git commit -m \"" + System.currentTimeMillis() + "\"", f, false);
        runCommand("git push", f, true);
        return null;
    }

    public void runCommand(String command, File f, boolean isStatusImportant) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command.toString(), null, f);

            InputStreamReader isr = new InputStreamReader(proc.getInputStream());
            BufferedReader rdr = new BufferedReader(isr);
            String line;
            while ((line = rdr.readLine()) != null) {
                firePropertyChange(Prop.LOG.name(), null, line);
                firePropertyChange(Prop.LOG.name(), null, '\n');
            }

            isr = new InputStreamReader(proc.getErrorStream());
            rdr = new BufferedReader(isr);
            while ((line = rdr.readLine()) != null) {
                firePropertyChange(Prop.LOG.name(), null, line);
                firePropertyChange(Prop.LOG.name(), null, '\n');
            }
//            boolean rc = proc.waitFor(1, TimeUnit.SECONDS);  // Wait for the process to complete
            int rc = proc.waitFor();  // Wait for the process to complete
            if (isStatusImportant) {
                firePropertyChange(Prop.STATUS.name(), null, rc);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     * Executed in event dispatching thread
     */
    @Override
    public void done() {
        firePropertyChange(Prop.FINISHED.name(), null, null);
    }
}