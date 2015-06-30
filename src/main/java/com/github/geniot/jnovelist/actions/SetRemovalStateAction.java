package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.ButtonTabComponent;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class SetRemovalStateAction extends AbstractNovelistAction implements KeyEventDispatcher {
    private boolean isEnabled;

    public SetRemovalStateAction(JNovelistFrame f) {
        super(f);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        boolean enableRemoval = (e.isAltDown() && e.isControlDown());


        DnDTabbedPane dnd = frame.dnDTabbedPane;
        Window[] wws = frame.getOwnedWindows();
        if (wws != null && wws.length > 0 && wws[0] instanceof JDialog) {
            if (((JDialog) wws[0]).getContentPane() instanceof DnDTabbedPane) {
                dnd = (DnDTabbedPane) ((JDialog) wws[0]).getContentPane().getComponent(0);
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
