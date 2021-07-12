package io.github.geniot.jnovelist;

import io.github.geniot.jnovelist.actions.PushTask;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SyncDialog extends JDialog implements PropertyChangeListener {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textAreaLog;
    private JNovelistApplication frame;
    private int procStatusTotal = 0;

    public SyncDialog(JNovelistApplication f) {
        this.frame = f;
        setIconImage(DesktopApplication.ICON);
        setTitle("Synchronization");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setPreferredSize(new Dimension(400, 300));
        pack();
        setLocationRelativeTo(frame);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        SyncDialog dialog = new SyncDialog(null);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PushTask.Prop.LOG.name())) {
            textAreaLog.append(evt.getNewValue().toString());
        } else if (evt.getPropertyName().equals(PushTask.Prop.STATUS.name())) {
            procStatusTotal += (Integer) evt.getNewValue();
        } else if (evt.getPropertyName().equals(PushTask.Prop.FINISHED.name())) {
            if (procStatusTotal == 0) {
                dispose();
            }
        }
    }
}
