package io.github.geniot.jnovelist.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.geniot.jnovelist.JNovelistApplication;
import org.apache.commons.io.FileUtils;

import java.io.File;

import static io.github.geniot.jnovelist.Utils.getGitRootDir;
import static io.github.geniot.jnovelist.Utils.runCommand;

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
                    File f = getGitRootDir(frame.path);
                    System.out.println(runCommand("git add -A", f));
                    System.out.println(runCommand("git commit -m \"" + System.currentTimeMillis() + "\"", f));
                    System.out.println(runCommand("git push", f));
                    System.out.println("Pushed in: " + (System.currentTimeMillis() - t1) + "ms");
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
