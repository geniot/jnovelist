package com.github.geniot.jnovelist.actions;

import com.github.geniot.jnovelist.ButtonTabComponent;
import com.github.geniot.jnovelist.DnDTabbedPane;
import com.github.geniot.jnovelist.JNovelistFrame;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class SetRemovalStateAction implements KeyEventDispatcher {
    private boolean isEnabled;
    protected JNovelistFrame frame;

    public SetRemovalStateAction(JNovelistFrame f) {
        this.frame = f;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        boolean enableRemoval = (e.getModifiers() & KeyEvent.ALT_MASK) != 0;
        if (enableRemoval != isEnabled && frame.dnDTabbedPane != null) {
            isEnabled = enableRemoval;
            for (int i = 0; i < frame.dnDTabbedPane.getTabCount(); i++) {
                Component c = frame.dnDTabbedPane.getTabComponentAt(i);
                if (c instanceof ButtonTabComponent) {
                    ButtonTabComponent btc = (ButtonTabComponent) c;
                    btc.enableRemoval(enableRemoval);
                }
                Component bookPartTab = frame.dnDTabbedPane.getComponentAt(i);
                if (bookPartTab instanceof DnDTabbedPane) {
                    DnDTabbedPane dnd = (DnDTabbedPane) bookPartTab;
                    for (int k = 0; k < dnd.getTabCount(); k++) {
                        Component o = dnd.getTabComponentAt(k);
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
