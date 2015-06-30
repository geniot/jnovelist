package com.github.geniot.jnovelist;

import com.github.geniot.jnovelist.model.PersistedModel;
import com.github.geniot.jnovelist.model.PersistedModel2;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Author: Vitaly Sazanovich
 * Email: vitaly.sazanovich@gmail.com
 * Date: 25/06/15
 */
public class Utils {
    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    public static JButton makeNavigationButton(String imageName,
                                               String actionCommand,
                                               String toolTipText,
                                               String altText) {
        //Look for the image.
        String imgLocation = "/images/" + imageName + ".png";
        URL imageURL = JNovelistLauncher.class.getResource(imgLocation);

        //Create and initialize the button.
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);

        if (imageURL != null) {                      //image found
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {                                     //no image found
            button.setText(altText);
            System.err.println("Resource not found: " + imgLocation);
        }

        return button;
    }

    public static Object xml2entry(String content) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes("UTF-8"));
            XMLDecoder decoder = new XMLDecoder(bais);
            Object eom = decoder.readObject();
            decoder.close();
            bais.close();
            return eom;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return new Object();
        }
    }

    public static String entry2xml(Object eom) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLEncoder encoder = new XMLEncoder(baos) {
                public PersistenceDelegate getPersistenceDelegate(Class<?> type) {
                    return super.getPersistenceDelegate(type);
                }
            };
            encoder.writeObject(eom);
            encoder.close();
            baos.close();
            return new String(baos.toByteArray(), "UTF-8");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return "";
        }
    }

    public static void updateStatus(final Component component) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Container c = component.getParent();
                while (c != null && !(c instanceof JNovelistFrame)) {
                    c = c.getParent();
                }
                if (c != null) {
                    ((JNovelistFrame) c).updateStatus();
                }
            }
        });


    }

    public static byte[] serialize(Object yourObject) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(yourObject);
            byte[] yourBytes = bos.toByteArray();
            return yourBytes;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return new byte[]{};
    }

    public static Object deserialize(byte[] yourBytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object o = in.readObject();
            return o;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return null;
    }

    public static PersistedModel2 loadModel(File selectedFile) {
        PersistedModel2 model = new PersistedModel2();
        if (selectedFile.exists()) {
            try {
                ZipFile zipFile = new ZipFile(selectedFile);
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    InputStream stream = zipFile.getInputStream(entry);
                    model = initModel(Utils.deserialize(IOUtils.toByteArray(stream)));
                    stream.close();
                }
                zipFile.close();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                try {
                    FileInputStream fis = new FileInputStream(selectedFile);
                    model = initModel(Utils.deserialize(IOUtils.toByteArray(fis)));
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return model;
    }

    private static PersistedModel2 initModel(Object o) {
        PersistedModel2 model = null;
        if (o instanceof PersistedModel2) {
            model = (PersistedModel2) o;
        } else {
            model = new PersistedModel2((PersistedModel) o);
        }
        return model;
    }
}
