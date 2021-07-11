package io.github.geniot.jnovelist.actions;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.geniot.jnovelist.JNovelistApplication;

public class CommitterTask implements Runnable {

    int projectHash;
    private final JNovelistApplication frame;

    public CommitterTask(JNovelistApplication frame) {
        this.frame = frame;
    }

    @Override
    public void run() {
        try {
            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
            prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
            String projectJSON = new ObjectMapper().writer(prettyPrinter).writeValueAsString(frame.novel);
            int hash = projectJSON.hashCode();
            if (hash != projectHash) {
                System.out.println(projectJSON);
                projectHash = hash;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
