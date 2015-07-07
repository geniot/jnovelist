package com.github.geniot.jnovelist;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.URL;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;


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

    public static String html2text(String html) {
        if (html == null) {
            return html;
        }

        StringBuilder sb = new StringBuilder();
        Elements ps = Jsoup.parse(html).select("p");
        for (Element el : ps) {
            String s = el.toString();
            s = s.replaceAll("<[^>]*>", "");
            s = s.replaceAll("\\n", "");
            s = s.trim();
            sb.append(s);
            sb.append("\n");
        }

        return sb.toString();
    }

    public static String text2html(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.HTML_DOC_START);
        String[] pps = text.split("\\n");
        for (String p : pps) {
            sb.append("<p>");
            sb.append(p);
            sb.append("</p>");
        }
        sb.append(Constants.HTML_DOC_END);
        return sb.toString();
    }

    public static Comparator<File> FILE_NAME_NUMBER_COMPARATOR = new Comparator<File>() {

        @Override
        public int compare(File o1, File o2) {
            Integer i1 = getInteger(o1.getName());
            Integer i2 = getInteger(o2.getName());
            if (i1 == null && i2 == null) {
                return o1.getName().compareTo(o2.getName());
            }
            if (i1 == null) {
                return 1;
            }
            if (i2 == null) {
                return -1;
            }
            return i1.compareTo(i2);
        }

        @Override
        public boolean equals(Object obj) {
            return this.equals(obj);
        }
    };


    public static Integer getInteger(String str) {
        try {
            return Integer.parseInt(str);

        } catch (NumberFormatException nfe) {
        }
        return null;
    }

    public static boolean textsEqual(String s1, String s2) {
        String tmp1 = s1.replaceAll("\n", "").replaceAll("\r", "");
        String tmp2 = s1.replaceAll("\n", "").replaceAll("\r", "");
        return tmp1.equals(tmp2);
    }

}
