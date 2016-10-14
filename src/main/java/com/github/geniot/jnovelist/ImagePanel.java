package com.github.geniot.jnovelist;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 07/04/14
 * Time: 11:09
 */
public class ImagePanel extends JPanel implements Serializable {
    Image image = null;

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g); //paint background
        if (image != null) { //there is a picture: draw it
            g.drawImage(image.getScaledInstance(this.getWidth(), -1, Image.SCALE_SMOOTH), 0, 0, this);
        }
    }
}
