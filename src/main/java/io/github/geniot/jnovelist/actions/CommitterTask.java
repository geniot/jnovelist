package io.github.geniot.jnovelist.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.geniot.jnovelist.JNovelistApplication;
import io.github.geniot.jnovelist.SyncDialog;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class CommitterTask implements Runnable {

    private int projectHash;
    private final JNovelistApplication frame;

    public CommitterTask(JNovelistApplication frame) {
        this.frame = frame;
    }

    @Override
    public void run() {
        save(false, false);
    }

    public synchronized void save(boolean push, boolean nullify) {
        try {
            if (frame.novel != null) {
                long t1 = System.currentTimeMillis();
                String projectJSON = getProjectJson();
                int hash = projectJSON.hashCode();
                if (hash != projectHash) {
                    projectHash = hash;
                    FileUtils.writeStringToFile(new File(frame.path), projectJSON);
                    System.out.println("Saved in: " + (System.currentTimeMillis() - t1) + "ms");
                }
                if (push) {
                    SyncDialog syncDialog = new SyncDialog(frame);
                    PushTask task = new PushTask(frame);
                    task.addPropertyChangeListener(syncDialog);
                    task.execute();
                    syncDialog.setVisible(true);
                }
                if (nullify) {
                    frame.setNovel(null, null);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getProjectJson() throws JsonProcessingException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        return new ObjectMapper().writer(prettyPrinter).writeValueAsString(frame.novel);
    }

}
