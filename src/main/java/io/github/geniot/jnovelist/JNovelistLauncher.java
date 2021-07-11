package io.github.geniot.jnovelist;


import javax.swing.*;

public class JNovelistLauncher {
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JNovelistApplication().setVisible(true);
            }
        });
    }
}
