package com.github.geniot.jnovelist;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 13/07/16
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 07/04/14
 * Time: 11:09
 */
public class ImagePanel extends JPanel implements Serializable {
    Image image = null;
    private double ratio1;
    private double ratio2;
    private int imageHeight;
    private int imageWidth;

    public ImagePanel() {
    }

    public ImagePanel(Image image) {
        this.image = image;
    }

    public void setImage(Image image) {
        this.image = image;

        imageHeight = ((BufferedImage) image).getHeight();
        imageWidth = ((BufferedImage) image).getWidth();
        ratio1 = (double) imageHeight / (double) imageWidth;
        ratio2 = (double) imageWidth / (double) imageHeight;

        repaint();
    }

    public Image getImage(Image image) {
        return image;
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g); //paint background
        if (image != null) { //there is a picture: draw it
            int height = this.getSize().height;
            int width = this.getSize().width;


            if (imageHeight > imageWidth) {//trying to squeeze height first
                if (ratio2 < 1) {
                    width = (int) (height * ratio2);
                }
            } else {
                if (ratio1 < 1) {
                    height = (int) (width * ratio1);
                }
            }

            g.drawImage(image, 0, 0, width, height, this);
            //g.drawImage(image, 0, 0, this); //original image size
        }  //end if
    } //end paint
} //end class
