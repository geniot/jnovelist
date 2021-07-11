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
            long t1 = System.currentTimeMillis();
            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
            prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
            String projectJSON = new ObjectMapper().writer(prettyPrinter).writeValueAsString(frame.novel);
            int hash = projectJSON.hashCode();
            if (hash != projectHash) {
                projectHash = hash;
                System.out.println("New hash: " + hash);
            }
            System.out.println(System.currentTimeMillis() - t1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
