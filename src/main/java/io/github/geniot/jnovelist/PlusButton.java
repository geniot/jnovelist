package io.github.geniot.jnovelist;

import javax.swing.*;

public class PlusButton extends JButton {
    public PlusButton() {
        super();
        this.setText("+");
        Utils.stylizeButton(this);
    }
}
