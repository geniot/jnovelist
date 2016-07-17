package com.github.geniot.jnovelist;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 13/07/16
 */
public class ImageEditor extends JPanel {
    ImagePanel imagePanel;

    public ImageEditor(File file) {
        super(new BorderLayout());
        try {
            imagePanel = new ImagePanel();
            imagePanel.setImage(readImage(file));
            add(imagePanel, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage readImage(File file) throws IOException {
        BufferedImage img = ImageIO.read(file);
        if (img == null) {
            byte[] bbs = IOUtils.toByteArray(new FileInputStream(file));
            ByteArrayInputStream bais = new ByteArrayInputStream(Utils.decryptBytes(bbs));
            img = ImageIO.read(bais);
            return img;
        } else {
            //encoding unencoded images
            byte[] bbs = IOUtils.toByteArray(new FileInputStream(file));
            byte[] encodedBytes = Utils.encryptBytes(bbs);
            FileUtils.writeByteArrayToFile(file, encodedBytes);

            return img;
        }

    }
}
