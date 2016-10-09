package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.ButtonTabComponent;
import com.github.geniot.jnovelist.Constants;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class KeyAction extends AbstractNovelistAction implements KeyEventDispatcher {
    private static final Logger logger = Logger.getLogger(KeyAction.class.getName());
    private boolean isEnabled;

    public KeyAction(JNovelistFrame f) {
        super(f);
    }

    public static boolean isModalDialogShowing() {
        Window[] windows = Window.getWindows();
        if (windows != null) { // don't rely on current implementation, which at least returns [0].
            for (Window w : windows) {
//                if (w.isShowing() && w instanceof JDialog && ((JDialog) w).isModal())
                if (w.isShowing() && w instanceof JDialog)
                    return true;
            }
        }
        return false;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {

        boolean enableRemoval = (e.isAltDown() && e.isControlDown());
        boolean isValid = e.getID() == KeyEvent.KEY_PRESSED && e.getModifiers() == 0;

        //minimize app on ESCAPE
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !isModalDialogShowing()) {
            if (!isValid) {
                return false;
            }
            frame.setState(Frame.ICONIFIED);
            return false;
        }

        //save on CTRL+S
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S && !isModalDialogShowing() && frame.saveNovel.isEnabled()) {
            for (final ActionListener a : frame.saveNovel.getActionListeners()) {
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.SAVE_NOVEL_ACTION_COMMAND) {
                    //Nothing need go here, the actionPerformed method (with the
                    //above arguments) will trigger the respective listener
                });
            }
            return false;
        }

        //call dictionary on CTRL+L
        if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_L && !isModalDialogShowing()) {
            for (ActionListener a : frame.dictionary.getActionListeners()) {
                a.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, Constants.DICTIONARY_ACTION_COMMAND) {
                    //Nothing need go here, the actionPerformed method (with the
                    //above arguments) will trigger the respective listener
                });
            }
            return false;
        }


        //enable CTRL+ALT mode to enable removal, dnd is enabled in DNDTabbedPane
        DnDTabbedPane dnd = frame.dnDTabbedPane;
        Window[] wws = frame.getOwnedWindows();
        for (Window w : wws) {
            if (w.isActive() && w instanceof JDialog) {
                JDialog jd = (JDialog) w;
                if (jd.getContentPane().getComponentCount() > 0 && jd.getContentPane().getComponent(0) instanceof DnDTabbedPane) {
                    dnd = (DnDTabbedPane) jd.getContentPane().getComponent(0);
                }
            }
        }

        if (enableRemoval != isEnabled && dnd != null) {
            isEnabled = enableRemoval;
            for (int i = 0; i < dnd.getTabCount(); i++) {
                Component c = dnd.getTabComponentAt(i);
                if (c instanceof ButtonTabComponent) {
                    ButtonTabComponent btc = (ButtonTabComponent) c;
                    btc.enableRemoval(enableRemoval);
                }
                Component bookPartTab = dnd.getComponentAt(i);
                if (bookPartTab instanceof DnDTabbedPane) {
                    DnDTabbedPane d = (DnDTabbedPane) bookPartTab;
                    for (int k = 0; k < d.getTabCount(); k++) {
                        Component o = d.getTabComponentAt(k);
                        if (o instanceof ButtonTabComponent) {
                            ButtonTabComponent btc = (ButtonTabComponent) o;
                            btc.enableRemoval(enableRemoval);
                        }
                    }
                }
            }
        }

        return false;
    }
}
